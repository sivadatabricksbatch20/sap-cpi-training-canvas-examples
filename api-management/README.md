# API Management labs

Five native **Classic API Management** proxies are deployed. Their 21 scenarios cover paths/query parameters, response transformation, API-key/product/app access, quotas/spike arrest and validation/fault responses.

**Recorded result: 19 native scenario passes; valid-key and revocation cases not run at the user’s request.** Their prerequisites and expected outcomes are documented.

Read [the student guide](../docs/API_MANAGEMENT_AND_MESH_GUIDE.md) and `catalogue.json` for fixtures and expected results. Use `manifest.json` for native revisions, exported archive checksums and exact runtime URLs. Live evidence is stored separately from local Javascript unit-test evidence.

Open **Configure → APIs** in Integration Suite to inspect Classic proxies. The direct editor route uses `/shell/configure/api/<proxy-name>`. Use **Engage → API Products / Applications** for the associated product and app, and **Test → APIs** for the test console.

These ZIPs use the Classic `APIProxy/` archive format. The newer package **Add → API → Integration Cell** wizard expects a different API-artifact format and will reject them. The dedicated `APIManagementMeshTraining` package contains the guide and source bundles as documents; the five native proxies live in Classic API Management.

For this trial, include the project path in the gateway URL:

```bash
export APIM_BASE_URL='https://e31930eatrial-trial.integrationsuitetrial-apim.ap21.hana.ondemand.com/e31930eatrial'
python api-management/run_tests.py --output /tmp/apim-live-results.json
```

Set `APIM_TRAINING_API_KEY` privately for the positive API-key case. The script reports missing prerequisites separately and rejects an ApplicationNotFound routing fault. Quota tests use a fresh classroom identifier per sequence; the header is client-controlled and is not a production identity mechanism.

To import into another authorized Classic tenant, provision API access and set `APIM_MANAGEMENT_BASE_URL` and `APIM_MANAGEMENT_TOKEN` in your secure shell:

```bash
python api-management/deploy.py TRN_API_001_ProxyBasics
```

Import/deployment acceptance is separate from live verification. Re-importing an existing proxy creates/replaces its native revision. Credentials, service keys and browser sessions are excluded from this repository.
