from dbt.cli.main import dbtRunnerResult, Manifest
import json
import sys
RUNNER_IMPORT as dbtRunner


args = json.loads(sys.argv[1])
kwargs = json.loads(sys.argv[2])
if ("compile", "--inline") == args[:2]:
    with open(kwargs["target_path"], "r") as f:
        manifest = Manifest.from_dict(json.read(f))
else:
    manifest = None
dbt = dbtRunner(manifest=manifest)
res: dbtRunnerResult = dbt.invoke(args, **kwargs)
if not res.success:
    raise res.exception
print(json.dumps(res.result.to_dict()))
