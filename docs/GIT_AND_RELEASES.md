# Git setup and release workflow

## Intended private repository

`https://github.com/sivadatabricksbatch20/sap-cpi-training-canvas-examples`

The connected GitHub integration returned `Resource not accessible by integration (createRepository)`. A local Git commit and portable source bundle can be prepared, but a remote repository must exist and be accessible before pushing. This is not an SAP authorization problem.

Create an empty **private** repository with the name above, without an initial README/license/gitignore. Grant the connected GitHub app access to it. Do not send passwords or personal access tokens through chat.

After the repository is available:

```bash
git remote add origin https://github.com/sivadatabricksbatch20/sap-cpi-training-canvas-examples.git
# Skip the add command if origin is already set.
git push -u origin main
git push origin training-snapshot-2026-11-17
```

## Native SAP Git Push

Observed in this tenant: package → Artifacts → artifact Actions → **Git Push**. Its dialog requests:

- Personal Access Token
- Repository Name
- Repository URL
- Branch
- Commit Message

Use a dedicated, least-privilege GitHub credential for the target private repository and enter it directly in SAP's secure credential field. The connected CLI integration token is not copied into SAP or committed to this repository. Native Git Push has not been claimed configured or tested while remote creation is blocked.

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
