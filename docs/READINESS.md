# Readiness and prerequisites

64 designs; 315 scenarios; 312 verified outcomes; three EDI conversion cases blocked. Native failures and observations count as verified only when recorded behaviour matches the documented expectation.

## EDI 056

Integration Advisor is active and trial-content-developer assigned. SAP still reports **“You do not have valid license for the type system”** for UN/EDIFACT. The matching annotated ORDERS D96A runtime XSD is unavailable.

`EDI56_NO_SCHEMA` is a verified diagnostic that intentionally fails at native resource lookup. It is not an EDI round trip. `EDI56_ORDER`, `EDI56_OTHER` and `EDI56_BAD` remain blocked.

With entitlement: create an ORDERS D96A MIG, export Cloud Integration runtime artifacts, upload the XSD, configure both converters, then rerun all three blocked cases. No licence purchase or contract change was made.

## Runtime prerequisites

- Deploy ProcessDirect receivers 049 and 053 before callers.
- Provision the RSA/PGP aliases referenced by security steps separately. Never commit private keys or credentials. Training keys do not establish production partner trust.
- Use live deployment for persistence, ID Mapping, variables, real ProcessDirect responses and native end states. Simulation mocks some behaviours.
- Aggregator 053 completes asynchronously. Lesson 057 generates a fresh batch per send: copy that exact value to `ExpectedBatchId` before readback. Verification deletes the result; unread results expire after two days.
- Idempotency keys and ID mappings persist. Use a fresh training key or clean its entry to repeat first-use behaviour.

## Coverage boundary

The handbook explains applicable flow tabs and component controls, distinguishing available choices from tested cases. Timer/ProcessDirect lessons cannot prove HTTP cookie reuse, external connectivity or production operation. Palette options outside the 64 designs have separate prerequisites.
