# API Management and mesh completion status

The extension contains five coded, natively deployed Classic API Management proxies with 21 documented scenarios, plus five exercises for each mesh product.

## Verification already completed

- **19 API scenarios passed on the native SAP gateway**, including proxy paths, query filtering, response transformation, headers, missing/invalid API keys, quota enforcement, spike arrest and validation/fault responses.
- **Two API scenarios were not run:** valid-key access and key revocation. The user requested documentation instead of additional testing. These need a dedicated Developer Hub application/key and the documented lifecycle action.
- Local checks completed earlier: 11 Javascript logic checks, two HTTP contract checks and two AMQP protocol-fixture checks. These are separate from SAP product runtime verification.

The five native proxy revisions and matching exports are recorded in `api-management/manifest.json`. Actual results and availability are in `tests/evidence/api-management-results.json` and `tests/evidence/extension-availability.json`.

## Mesh prerequisites

SAP Event Mesh and advanced event mesh are absent from the trial’s available service-plan assignments; no instances are provisioned. Their ten exercises, synthetic event, clients and configuration instructions are prepared, but **no native SAP broker success is claimed**.

The optional official local Solace broker also could not start in this managed workspace because its required kernel interface was unavailable. The included local AMQP fixture is a protocol test, not a replacement broker.

## Where to find the materials

- SAP Integration Suite package: **API Management and Mesh - Training** (`APIManagementMeshTraining`), Documents.
- Native Classic proxy editor: **Configure → APIs**, or `/shell/configure/api/<proxy-name>`.
- API products/applications: **Engage**. Developer Hub onboarding was completed; create the dedicated training application there before positive-key/lifecycle exercises.
- Gateway base URL for this trial: `https://e31930eatrial-trial.integrationsuitetrial-apim.ap21.hana.ondemand.com/e31930eatrial`.
- The package’s newer **Add → API → Integration Cell** wizard uses a different archive format; it does not import the Classic APIProxy ZIPs. Source ZIPs are attached as documents and stored unpacked in Git.

The original 64 CPI iFlows and their existing scenario evidence remain separate from this extension.
