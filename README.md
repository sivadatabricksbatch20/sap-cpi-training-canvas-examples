# SAP CPI Training — Canvas Examples

Practical SAP Cloud Integration lessons for `CPITrainingCanvasExamples`.

## Delivery status

- **64 lesson designs; 315 scenarios; 312 verified outcomes**, including positive assertions, intentional failures and measured behaviours.
- The remaining 59 lessons now use practical fixtures, configuration variants, assertions and explanations, extending the original five-flow review pattern.
- **63 lessons have working practical exercises.** EDI lesson **056** has a tested missing-schema diagnostic; three conversion cases require the trial’s unavailable UN/EDIFACT licence/runtime XSD.
- Aggregator **053** works asynchronously and is verified through **057**: ordered delivery, out-of-order delivery, timeout and durable readback. Externalization **059** works with `ScenarioId` and `GreetingPrefix`.
- Datastore, variables, ID Mapping, idempotency, ProcessDirect and native end states were checked live. Simulation limitations are labelled.
- The separate original `CPITraining/IF_Hello_World` was preserved.

Start with the [Complete Canvas Lab Handbook](docs/Complete_Canvas_Lab_Handbook.md) ([Word document](docs/Complete_Canvas_Lab_Handbook.docx)): flow tabs, component controls, scenarios, inputs, outputs, Groovy debugging, dependencies and DEV → UAT → PROD practices.

## Run a lesson

1. Import into an authorized tenant or open the existing package. Provision training security aliases separately; keys and credentials are excluded from Git.
2. Choose **Configure → ScenarioId**, enter a documented case ID, save and deploy. Timer lessons run once per deployment.
3. Inspect `TRN_ScenarioId`, `TRN_Expected` and `TRN_TestResult` in monitoring. Intentional rejections finish `FAILED`; escalation finishes `ESCALATED`.
4. Use Simulation for full JSON reports; use live deployment for persistence, real calls and end-state/aggregation proof.
5. Deploy receivers **049** and **053** first. Exercise 049 through 048/050/051. Follow the handbook’s send → copy batch → verify protocol for 053/057; verification consumes the result.
6. Restore the documented default after alternatives. Repeated idempotency/ID-mapping keys retain state.

## Repository layout

- `iflows/<technical-id>/`: unpacked SAP export: `.iflw`, scripts, mappings, schemas and parameter defaults.
- `exports/`: exact SAP package export for backup/import.
- `docs/`: complete handbook, readiness and Git/release guidance. Earlier baseline/five-flow documents are historical.
- `tests/scenario-catalog.json`: full fixtures and assertions for all 315 cases.
- `tests/evidence/`: sanitized simulation/live results, default checks, design validation and persistence/aggregation proof.
- `manifest.json`: exact exported versions and readiness.
- `.github/workflows/validate.yml`: offline validation for pushes and PRs; no SAP deployment.

## Versions and releases

Expanded lessons are **3.0.0**, with Timer 021 at **3.0.1**. Pilot processing is retained: 002/003 export at **2.0.2** (existing draft settings match the verified pilot); 004–006 remain **2.0.1**. The manifest is authoritative.

Use feature branches and PRs. Keep code, fixtures, assertions and docs together. Save the CPI artifact version before exporting, then run:

```bash
python scripts/validate_repository.py
```

Promote the same approved artifact through UAT and PROD using the project transport/CI mechanism. Environment endpoints and security material are separate. UAT/PROD deployment is not configured.

Native SAP **Git Access** requires a dedicated credential entered directly in SAP. This export push does not establish the tenant association. See [Git and releases](docs/GIT_AND_RELEASES.md).

Coverage is representative, not every possible input/adapter combination. Operation Mapping and other palette options outside these 64 designs require additional resources or systems. See the handbook for tested controls and boundaries.
