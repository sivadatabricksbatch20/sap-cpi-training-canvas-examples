# Git setup and release workflow

## Private repository

https://github.com/sivadatabricksbatch20/sap-cpi-training-canvas-examples

The repository holds the complete training source snapshot. Clone it using your authorized GitHub identity:

```bash
git clone https://github.com/sivadatabricksbatch20/sap-cpi-training-canvas-examples.git
cd sap-cpi-training-canvas-examples
python scripts/validate_repository.py
```

For future changes, create a feature branch, commit exported sources and related tests, push the branch and open a pull request. Do not send passwords or personal access tokens through chat.

## Tenant-level Git Access configuration (required)

Verified in this tenant: **Settings → Integrations → Git Access**. The Repository Details table was empty when checked; uploading exported source to GitHub does not populate this table or associate an iFlow with Git.

1. Open Settings → Integrations → Git Access.
2. Choose Edit, then Add.
3. Enter a dedicated GitHub PAT directly in SAP and the repository URL above.
4. Choose Connect, review the populated repository details, then Add and Save.
5. Return to the package Artifacts tab. Use an artifact's Git Push with the configured repository and branch `cpi-sync` for the initial native integration test. Git Pull requires an existing Git association.

The `cpi-sync` branch exists for this native-layout test; `main` contains the reviewed export layout. Tenant-level configuration and native push still require the dedicated PAT and have not been completed merely by the GitHub CLI upload.

Official instructions: https://help.sap.com/docs/integration-suite/isuite-integrations-and-apis/git-access-ga

## Native SAP Git Push

Observed in this tenant: package → Artifacts → artifact Actions → **Git Push**. Its dialog requests:

- Personal Access Token
- Repository Name
- Repository URL
- Branch
- Commit Message

Use a dedicated, least-privilege GitHub credential for the target private repository and enter it directly in SAP's secure credential field. The connected CLI integration token is not copied into SAP or committed to this repository. The GitHub repository and local origin are configured. Native SAP Git Push authentication remains separate and has not been configured or tested; enter a dedicated repository credential directly in SAP when using that feature.

A native SAP Git push may use a different artifact directory layout from the review-oriented unpacked `iflows/` tree. Test a single artifact on a separate branch before combining both approaches. Do not overwrite `main` from an unverified native layout.

## Branch and version convention

- `main`: reviewed training source.
- `feature/<topic>`: new scenarios or script changes.
- `fix/<issue>`: compatible fixes.
- Git release tags identify a repository snapshot; CPI artifact versions identify individual designs.

For changes: branch → edit/test in DEV → export matching version → validate → pull request → review → merge → release tag. Promote the same approved artifact to UAT and then PROD using the organization's transport/CI mechanism. Configure environment endpoints and provision security material separately.

Do not enable automatic deployment to PROD from this example repository. The included CI only validates the exported source tree. UAT/PROD tenants and deployment credentials have not been supplied or configured.

## Rollback

Select a previously approved commit/tag and the corresponding CPI artifact version, import and redeploy through the approved process. Restore environment configuration where necessary. Code rollback does not undo messages already processed, datastore mutations or receiver-system changes.
