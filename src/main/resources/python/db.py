from dbt.cli.main import cli, global_flags
from dbt.task.debug import DebugTask
from dbt.cli import requires, params as p
from dbt.adapters.factory import get_adapter, register_adapter
import click
import agate
from typing import Iterable, Tuple
import json


class RunQuery(DebugTask):

    @staticmethod
    def serialize_table(table: agate.Table) -> Iterable[Tuple]:
        yield tuple(str(_) for _ in table.columns.keys())
        for row in table.rows.values()[:1000]:
            yield tuple(str(_) for _ in row.values())

    def run(self) -> bool:
        self._load_profile()
        register_adapter(self.profile)
        adapter = get_adapter(self.profile)
        with adapter.connection_named('debug'):
            # conn = adapter.connections.get_thread_connection()
            # creds = conn.credentials.to_dict()
            fetch = self.args._plugin_custom_fetch == "true"
            adapter_response, table = adapter.execute(self.args._plugin_custom_sql, fetch=fetch)
            if fetch:
                print(json.dumps(dict(adapterResponse=adapter_response.to_dict(), data=list(self.serialize_table(table)))))
            else:
                print(json.dumps(dict(adapterResponse=adapter_response.to_dict())))
            return True


_plugin_custom_sql = click.option(
    "--_plugin_custom_sql",
    envvar=None,
)


_plugin_custom_fetch = click.option(
    "--_plugin_custom_fetch",
    envvar=None,
)


@cli.command("run_query")
@click.pass_context
@global_flags
@p.debug_connection
@p.config_dir
@p.profile
@p.profiles_dir_exists_false
@p.project_dir
@p.target
@p.vars
@_plugin_custom_sql
@_plugin_custom_fetch
@requires.postflight
@requires.preflight
def run_query_custom(ctx, **kwargs):
    task = RunQuery(
        ctx.obj["flags"],
        None,
    )
    results = task.run()
    success = task.interpret_results(results)
    return results, success


if __name__ == "__main__":
    cli()
