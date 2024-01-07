from dbt.cli.main import dbtRunnerResult
import json
import sys
RUNNER_IMPORT as dbtRunner


dbt = dbtRunner()
args = json.loads(sys.argv[1])
kwargs = json.loads(sys.argv[2])
res: dbtRunnerResult = dbt.invoke(args, **kwargs)
if not res.success:
    raise res.exception
