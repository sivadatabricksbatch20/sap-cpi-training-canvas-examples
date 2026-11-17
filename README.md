# SAP CPI Training — Canvas Examples

Source-controlled snapshot of the `CPITrainingCanvasExamples` SAP Cloud Integration package.

Private repository: https://github.com/sivadatabricksbatch20/sap-cpi-training-canvas-examples

## Delivery status

- 64 teaching iFlows.
- Lessons 002–006 are review pilot **2.0.1**, with 49 scenarios: 42 assertion passes, five expected rejections and two documented decoder behaviours.
- All five pilot defaults completed on the live runtime. The other 59 retain their baseline teaching designs.
- Baseline limitations: Aggregator 053 has an asynchronous Camel runtime error; EDI conversion 056 needs a matching annotated schema; Externalization 059 remains a baseline troubleshooting lesson. The pilot flows' externalized `ScenarioId` works.
- Operation Mapping, external polling, EDI validation and IDoc splitting have documented prerequisites rather than a complete runnable implementation.

## Repository layout

- `iflows/<technical-id>/`: unpacked SAP export, including `.iflw`, bundled Groovy/JavaScript, XSLT/mappings, schemas and default parameters.
- `exports/`: SAP package export for backup/import; unpacked sources are used for code review.
- `docs/`: baseline student guide, five-flow review document, Git and release instructions.
- `tests/`: scenario catalogue and sanitized test evidence; no session cookies, credentials or private keys.
- `manifest.json`: exact exported artifact versions and readiness.
- `.github/workflows/validate.yml`: offline validation on pushes and pull requests; it does not deploy to SAP.

## Run the pilot

1. Import the package into an authorized Cloud Integration tenant, or open the existing training package.
2. Provision the documented security dependencies separately; keys and passwords are not part of this repository.
3. Configure `ScenarioId`: defaults `JX01`, `XJ01`, `XP01`, `BE01`, `BD01` for lessons 002–006.
4. Deploy a lesson. Its timer fires once. Inspect `TRN_ScenarioId` and `TRN_TestResult` in message monitoring.
5. Use Simulation to inspect the full JSON assertion report. Documented rejection scenarios deliberately fail.

See **Five iFlow Review Pack - All Tabs** for exact inputs, outputs, settings and test boundaries. The baseline handbook predates the expanded five-flow harness.

## Versioning and changes

Use feature branches and pull requests. Keep Groovy changes and their fixtures/assertions in the same PR. Save a CPI artifact version before exporting. Record its version in the manifest; Git commits/tags and deployed CPI versions are separate identities.

This export includes metadata-only versioned snapshots for previously draft baseline artifact descriptions. Their processing logic was not changed or redeployed. The five review flows remain at 2.0.1.

Run:

```bash
python scripts/validate_repository.py
```

The initial Git commit is a source snapshot, not a DEV→UAT→PROD release. Do not deploy the unresolved prerequisite/troubleshooting labs as production integrations.
