# Complete Canvas Lab Handbook

17 November 2026 · 64 lesson designs. Pilot processing is retained: 002/003 export at 2.0.2 and 004–006 at 2.0.1. Expanded lessons are 3.0.0, with Timer 021 at 3.0.1. 63 lessons have working practical exercises. Lesson 056 has a verified missing-schema diagnostic; three EDI conversion cases require the trial’s unavailable UN/EDIFACT licence and runtime XSD.

315 scenario definitions: 49 pilot scenarios and 266 new scenarios. 312 have recorded verification, including expected failures and measured behaviours; three are blocked. This is representative coverage of the canvas topics and applicable controls, not every possible input or adapter combination.

# How to run the lessons

Open CPI Training - Canvas Examples → Artifacts. Read a lesson objective and identify its native steps.

Choose Configure → ScenarioId. Copy an ID from its scenario table, save and Deploy. Timer lessons run once per deployment. Restore the first/default case after trying alternatives.

Open Monitor → Message Processing. Inspect TRN_ScenarioId, TRN_Expected and TRN_TestResult. Positive assertions report PASS; negative cases intentionally finish FAILED and escalation finishes ESCALATED.

Simulation: select the connector before Set Sample Input and the connector after Assert and Explain. Supply the scenario ID. Reports contain input, native output, Java type, byte count, Base64 and SHA-256. Splitter cases may return multiple reports.

Use live deployment for persistence, ID Mapping, variables, ProcessDirect, native end states and asynchronous Aggregator completion. The simulator mocks some resources and receiver responses.

Deploy receivers 049 and 053 before their callers. Their default is LIVE and they have no timer. Use 048/050/051 to exercise 049, and the 057 protocol to exercise 053.

# Every integration-flow tab

| Tab | Meaning and exercise | Verification boundary |
|---|---|---|
| General | Stable technical ID, readable name, description and saved version. Identify the processing sequence. | Saved metadata and versions checked. The separate original CPITraining/IF_Hello_World is preserved. |
| Runtime Configuration | Namespace Mapping supports XPath namespaces. Inspect Allowed Headers, HTTP Session Reuse, runtime profile and transaction handling where applicable. | Namespaces and header/property values are exercised. HTTP cookie reuse needs an HTTP receiver. Aggregation retains JDBC transaction handling. |
| Error Configuration | Return Exception to Sender depends on the sender. Compare handled errors 035/036, Error End 037, Escalation 038, Terminate 039 and End 040. | Actual MPL statuses tested. A timer cannot demonstrate an HTTP exception response. |
| References | Trace local processes, scripts, schemas, mappings and adapter references. Open the resources and locate their use. | Resources exported. Receiver addresses and security material require tenant configuration. |
| Externalized Parameters | ScenarioId selects fixtures; 059 also exposes GreetingPrefix, 053 AggregationTimeoutMinutes, and 057 ExpectedBatchId. | Saved selectors and live Configure tested. Receiver ScenarioId is descriptive; 053 inputs come through 057. |
| Problems | Distinguish errors from warnings. Locate the referenced element and revalidate. | Final audit records saved warnings and checks deploy-restricting errors. |
| More / additional tabs | Resources, references and other controls can move under More with editor width and SAP release. | Use the controls in this tenant; placement can differ. |

# Step tabs and complete control inventory

General identifies a step; Processing contains its component settings. Adapters add Connection, Processing and Security where supported. Content Modifier separates Message Header, Exchange Property and Message Body. The per-lesson inventory lists controls, choices, saved example values and conditional visibility from this tenant’s metadata. Listing a choice does not mean every possible setting was independently runtime-tested.

# Routing progression

| Canvas option | Lessons | Teaching point |
|---|---|---|
| Router | 023; configuration branches elsewhere | Mutually exclusive routes, defaults and boundary inputs. |
| Multicast / Join / Gather | 024,031,014–016 | Parallel and sequential branches, synchronization, combining results and ordering. |
| Splitter | 014,015,016,044,045,055,065 | General/Iterating, line, archive, EDI and CMS parts; multiple, empty, malformed and Unicode inputs where applicable. |
| Local and looping process | 022,032,033,034 | Do-while executes once before testing; maximum iterations intentionally fail; idempotency uses persisted source IDs. |
| Aggregator | 053+057 | Correlation, sequence, last marker, asynchronous completion and timeout with durable readback. |

# Aggregator: run and prove the result

Deploy 053, then 057 with TRIGGER57_ORDERED. Copy the new <batch> value from the successful 057 MPL TRN_OutputPreview.

Configure 057 ScenarioId=TRIGGER57_VERIFY and ExpectedBatchId=<copied batch>; deploy. Native datastore Get reads the actual aggregate and deletes it. The verifier asserts sequence 1,2.

Repeat using TRIGGER57_REVERSED. Sequence 2, correctly marked last, arrives before sequence 1; the stored result is still sequenced.

Run TRIGGER57_TIMEOUT, copy its fresh batch, wait more than one minute, then deploy TRIGGER57_VERIFY_TIMEOUT with that batch. Only sequence 1 is expected.

TRIGGER57_BAD_LAST deliberately sends an inconsistent last marker and must fail.

Always use the batch from the same send execution. Verification consumes the result; a second Get should not succeed. Unread results expire after two days.

# Debug Groovy

Reproduce one documented ScenarioId and open the MPL error details. Inspect the script filename and line number. LAB_ASSERTION means a verification failed; it does not count as an expected native rejection.

Enable Trace temporarily and inspect payload, headers and properties immediately around the script. Disable Trace after troubleshooting. Avoid logging credentials or personal production data.

Use the simulation report to inspect byte encoding, Java types and property values. Keep business scripts separate from LoadLabScenario and VerifyLabScenario. Check malformed XML, missing properties, wrong namespaces and byte/String conversions.

Use local Groovy unit tests with a mocked SAP Message interface for isolated logic. Such tests cannot prove SAP adapters, persistence, keystore access or runtime transactions.

Change one value at a time, predict the result and compare. Try blank, special-character, Unicode and multi-record inputs.

# Security, Git and promotion

Fixtures are synthetic. Training RSA alias trn_canvas_rsa and existing PGP aliases are tenant prerequisites. Private keys, passwords and tokens are excluded from exports. Simple Signer is independently checked using the public certificate; tamper cases exercise native rejection. Production partner trust and key rotation remain project responsibilities.

Feature branch → test → export matching saved CPI versions → validate → pull request → review → release. Promote the same approved artifact to UAT and PROD through the project transport/CI system. Externalize environment endpoints and provision security material separately. UAT/PROD deployment is not configured here.

Native SAP Git Access remains separate from this GitHub snapshot. Settings → Integrations → Git Access → Edit → Add requires a dedicated GitHub credential entered directly in SAP. Uploading exports does not establish an artifact association. main is the reviewed export layout; cpi-sync is reserved for a native-layout test.

# 056: licence prerequisite

Integration Advisor is active and trial-content-developer assigned. Discover → Type Systems → UN/EDIFACT returns “You do not have valid license for the type system.” No plain XSD was fabricated as a replacement. EDI56_NO_SCHEMA verifies the native resource-lookup failure, not a successful conversion. With an entitled ORDERS D96A MIG, export Cloud Integration runtime artifacts, upload the XSD, point both converters to it, then rerun ORDER, OTHER and BAD. No licence purchase or contract change was made.

# Lesson index

| Lesson | Scenarios | Readiness |
|---|---|---|
| TRN 001 - Hello World | 4 | Verified |
| TRN 002 - JSON to XML | 9 | Verified |
| TRN 003 - XML to JSON | 11 | Verified |
| TRN 004 - XPath Filter | 10 | Verified |
| TRN 005 - Base64 Encode | 8 | Verified |
| TRN 006 - Base64 Decode | 11 | Verified |
| TRN 007 - GZIP Roundtrip | 6 | Verified |
| TRN 008 - ZIP Roundtrip | 6 | Verified |
| TRN 009 - XML Declaration | 6 | Verified |
| TRN 010 - XML to CSV | 7 | Verified |
| TRN 011 - XML JSON Roundtrip | 5 | Verified |
| TRN 012 - Base64 Roundtrip | 5 | Verified |
| TRN 013 - SHA256 Digest | 8 | Verified |
| TRN 014 - General Splitter Gather | 6 | Verified |
| TRN 015 - Iterating Splitter Gather | 6 | Verified |
| TRN 016 - Line Splitter Gather | 5 | Verified |
| TRN 017 - Data Store Roundtrip | 4 | Verified |
| TRN 018 - Data Store Missing Entry | 3 | Verified |
| TRN 019 - Persist Message | 4 | Verified |
| TRN 020 - MIME Roundtrip | 5 | Verified |
| TRN 021 - Timer Hello | 3 | Verified |
| TRN 022 - Local Process Call | 4 | Verified |
| TRN 023 - Router | 6 | Verified |
| TRN 024 - Parallel Multicast | 4 | Verified |
| TRN 025 - Groovy Uppercase | 5 | Verified |
| TRN 026 - JavaScript Uppercase | 5 | Verified |
| TRN 027 - XSLT Mapping | 5 | Verified |
| TRN 028 - XML Validator Valid | 6 | Verified |
| TRN 029 - XML Validator Invalid | 6 | Verified |
| TRN 030 - CSV to XML | 6 | Verified |
| TRN 031 - Sequential Multicast | 4 | Verified |
| TRN 032 - Looping Process Call | 4 | Verified |
| TRN 033 - Loop Maximum Guard | 4 | Verified |
| TRN 034 - Idempotent Process Call | 3 | Verified |
| TRN 035 - Exception Handled | 4 | Verified |
| TRN 036 - Exception Rethrown | 4 | Verified |
| TRN 037 - Error End Event | 3 | Verified |
| TRN 038 - Escalation End Event | 3 | Verified |
| TRN 039 - Terminate Message | 3 | Verified |
| TRN 040 - Plain End Event | 3 | Verified |
| TRN 041 - ID Mapping | 3 | Verified |
| TRN 042 - Write Variables | 4 | Verified |
| TRN 043 - Data Store Select | 3 | Verified |
| TRN 044 - ZIP Splitter | 5 | Verified |
| TRN 045 - Tar Splitter | 5 | Verified |
| TRN 046 - Headers Properties | 4 | Verified |
| TRN 047 - XPath Property | 4 | Verified |
| TRN 048 - Request Reply | 6 | Verified |
| TRN 049 - Local Mock Receiver | 3 | Verified |
| TRN 050 - Send Local Message | 4 | Verified |
| TRN 051 - Content Enricher | 4 | Verified |
| TRN 053 - Aggregator | 2 | Verified |
| TRN 054 - EDI Extractor | 4 | Verified |
| TRN 055 - EDI Splitter | 4 | Verified |
| TRN 056 - EDI XML Roundtrip | 4 | Conversion blocked; diagnostic verified |
| TRN 057 - Aggregator Trigger | 6 | Verified |
| TRN 058 - Message Mapping | 5 | Verified |
| TRN 059 - Externalized Parameter | 4 | Verified |
| TRN 060 - CMS Encrypt Decrypt | 5 | Verified |
| TRN 061 - CMS Sign Verify | 5 | Verified |
| TRN 062 - Simple Signer | 4 | Verified |
| TRN 063 - XML Sign Verify | 4 | Verified |
| TRN 064 - PGP Encrypt Decrypt | 5 | Verified |
| TRN 065 - CMS Signature Splitter | 4 | Verified |

# TRN 001 - Hello World

Technical ID: IF_TRN_001_Hello_World. Version 3.0.0.

Default Configure → ScenarioId=H01. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| H01 | Build a greeting from properties | base | SIMULATION: PASS |
| H02 | Header/property substitution | base | SIMULATION: PASS |
| H03 | Unicode greeting | base | SIMULATION: PASS |
| H04 | Empty optional name | base | SIMULATION: PASS |

## H01 — Build a greeting from properties

Input / fixture:

```text
[empty body]
```

Properties: {"StudentName": "student", "GreetingPrefix": "Hello"}

Expected checks: {"exact": "Hello, student!"}

Recorded: SIMULATION / PASS

Native output:

```text
Hello, student!
```

Java type: java.lang.String; bytes: 15

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## H02 — Header/property substitution

Input / fixture:

```text
[empty body]
```

Properties: {"StudentName": "Ravi", "GreetingPrefix": "Welcome"}

Expected checks: {"exact": "Welcome, Ravi!"}

Recorded: SIMULATION / PASS

Native output:

```text
Welcome, Ravi!
```

Java type: java.lang.String; bytes: 14

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## H03 — Unicode greeting

Input / fixture:

```text
[empty body]
```

Properties: {"StudentName": "సాయి", "GreetingPrefix": "Hello"}

Expected checks: {"exact": "Hello, సాయి!"}

Recorded: SIMULATION / PASS

Native output:

```text
Hello, సాయి!
```

Java type: java.lang.String; bytes: 20

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## H04 — Empty optional name

Input / fixture:

```text
[empty body]
```

Properties: {"StudentName": "", "GreetingPrefix": "Hello"}

Expected checks: {"exact": "Hello, !"}

Recorded: SIMULATION / PASS

Native output:

```text
Hello, !
```

Java type: java.lang.String; bytes: 8

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.GreetingPrefix}, ${property.StudentName}! | Enter an expression or a constant to form new message body | When applicable |

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/LoadLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

Saved design messages: []

# TRN 002 - JSON to XML

Technical ID: IF_TRN_002_JSON_to_XML. Version 2.0.2.

Default Configure → ScenarioId=JX01. Save and deploy once.

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| JX01 | Nested sales order | plain | PILOT_SIMULATION: PASS |
| JX02 | Repeated order items | plain | PILOT_SIMULATION: PASS |
| JX03 | Unicode and XML escaping | plain | PILOT_SIMULATION: PASS |
| JX04 | Boolean, empty string and JSON null | plain | PILOT_SIMULATION: PASS |
| JX05 | Add a root to sibling JSON fields | wrap | PILOT_SIMULATION: PASS |
| JX06 | Colon-prefixed namespace mapping | ns | PILOT_SIMULATION: PASS |
| JX07 | Dot as JSON prefix separator | dot | PILOT_SIMULATION: PASS |
| JX08 | Malformed JSON is rejected | plain | PILOT_SIMULATION: EXPECTED_REJECTION |
| JX09 | JSON key invalid as an XML name | plain | PILOT_SIMULATION: EXPECTED_REJECTION |

## JX01 — Nested sales order

Input / fixture:

```text
{"order":{"id":"SO1001","customer":{"name":"Anu"},"amount":250}}
```

Expected checks: {"xmlRoot": "order", "contains": ["SO1001", "Anu", "250"]}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><order><id>SO1001</id><customer><name>Anu</name></customer><amount>250</amount></order>
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 125

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## JX02 — Repeated order items

Input / fixture:

```text
{"order":{"items":[{"sku":"A1","qty":2},{"sku":"B2","qty":1}]}}
```

Expected checks: {"xmlRoot": "order", "contains": ["A1", "B2"]}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><order><items><sku>A1</sku><qty>2</qty></items><items><sku>B2</sku><qty>1</qty></items></order>
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 133

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## JX03 — Unicode and XML escaping

Input / fixture:

```text
{"customer":{"name":"సాయి José 😀","note":"A & B < C"}}
```

Expected checks: {"xmlRoot": "customer", "contains": ["సాయి José 😀", "A & B < C"]}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><customer><name>సాయి José 😀</name><note>A &amp; B &lt; C</note></customer>
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 124

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## JX04 — Boolean, empty string and JSON null

Input / fixture:

```text
{"order":{"active":true,"note":"","optional":null}}
```

Expected checks: {"xmlRoot": "order", "contains": ["true"]}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><order><active>true</active><note></note><optional/></order>
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 98

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## JX05 — Add a root to sibling JSON fields

Input / fixture:

```text
{"id":"SO1002","amount":10}
```

Expected checks: {"xmlRoot": "orders", "contains": ["SO1002", "10"]}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><orders><id>SO1002</id><amount>10</amount></orders>
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 89

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## JX06 — Colon-prefixed namespace mapping

Input / fixture:

```text
{"p:order":{"p:id":"SO1003"}}
```

Expected checks: {"xmlRoot": "order", "xmlNamespace": "urn:training:orders", "contains": ["SO1003"]}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><p:order xmlns:p="urn:training:orders"><p:id>SO1003</p:id></p:order>
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 106

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## JX07 — Dot as JSON prefix separator

Input / fixture:

```text
{"p.order":{"p.id":"SO1004"}}
```

Expected checks: {"xmlRoot": "order", "xmlNamespace": "urn:training:orders", "contains": ["SO1004"]}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><p:order xmlns:p="urn:training:orders"><p:id>SO1004</p:id></p:order>
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 106

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## JX08 — Malformed JSON is rejected

Input / fixture:

```text
{"order":
```

Expected checks: {"negative": true}

Recorded: PILOT_SIMULATION / EXPECTED_REJECTION

```text
End of input at line 1 column 10 path $.order on the exchange: Exchange[CC146CCF5AB4018-0000000000000001]
```

Native output:

```text
[empty body]
```

Java type: ; bytes: 

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## JX09 — JSON key invalid as an XML name

Input / fixture:

```text
{"1invalid":"x"}
```

Expected checks: {"negative": true}

Recorded: PILOT_SIMULATION / EXPECTED_REJECTION

```text
The JSON member name "1invalid" cannot be converted to an XML element name or attribute name, because it contains invalid XML name characters. Instead of using the JSON-to-XML Converter, use the Message Mapping to convert the JSON document. on the exchange: Exchange[8EA0972A5C85B05-0000000000000001]
```

Native output:

```text
[empty body]
```

Java type: ; bytes: 

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### JSON To XML Converter

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Use Namespace Mapping | false | Use namespace mapping to define a relation between the JSON prefix and the XML namespace | When applicable |
| Processing | Namespace Mapping | [] | Enter the mapping of the JSON prefix to the XML namespace | Conditional |
| Processing | JSON Prefix |  |  | When applicable |
| Processing | XML Namespace |  |  | When applicable |
| Processing | JSON Prefix Separator | : | Colon(:), Comma(,), Dot(.), Pipe(\|), Semicolon(;), Space | When applicable |
| Processing | Add XML Root Element | false | Add a root element to the produced document | When applicable |
| Processing | Name | root | Enter the name of the root element | Conditional |
| Processing | Namespace Mapping |  | Select the namespace mapping for the root element | Conditional |

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | constant | Expression, Constant | When applicable |
| Message Body | Body | {"customer":{"name":"Anu"}} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: [{"severity": "Warning", "message": "Content Modifier may not pass JSON message to JSON To XML Converter. JSON To XML Converter supports JSON input only.", "elementId": "CallActivity_11", "elementType": "CallActivity", "location": "JsonToXmlConverter", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 002 - JSON to XML.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}]

# TRN 003 - XML to JSON

Technical ID: IF_TRN_003_XML_to_JSON. Version 2.0.2.

Default Configure → ScenarioId=XJ01. Save and deploy once.

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| XJ01 | Sales order and JSON value types | plain | PILOT_SIMULATION: PASS |
| XJ02 | Repeated elements become an array | plain | PILOT_SIMULATION: PASS |
| XJ03 | Suppress the enclosing root key | suppress | PILOT_SIMULATION: PASS |
| XJ04 | Force all leaf elements into arrays | arrays | PILOT_SIMULATION: PASS |
| XJ05 | Keep a one-item field as an array | specific | PILOT_SIMULATION: PASS |
| XJ06 | Streaming conversion with repeated items | stream | PILOT_SIMULATION: PASS |
| XJ07 | Map XML namespaces to colon JSON keys | ns | PILOT_SIMULATION: PASS |
| XJ08 | UTF-16LE output bytes | utf16 | PILOT_SIMULATION: PASS |
| XJ09 | Empty elements and boolean-looking text | plain | PILOT_SIMULATION: PASS |
| XJ10 | Malformed XML is rejected | plain | PILOT_SIMULATION: EXPECTED_REJECTION |
| XJ11 | Dot JSON namespace separator | dot | PILOT_SIMULATION: PASS |

## XJ01 — Sales order and JSON value types

Input / fixture:

```text
<order><id>SO1001</id><amount>250</amount></order>
```

Expected checks: {"jsonRoot": "order", "contains": ["SO1001", "250"]}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
{"order":{"id":"SO1001","amount":"250"}}
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 40

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XJ02 — Repeated elements become an array

Input / fixture:

```text
<order><item>A1</item><item>B2</item></order>
```

Expected checks: {"jsonRoot": "order", "arrayPath": ["order", "item"]}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
{"order":{"item":["A1","B2"]}}
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 30

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XJ03 — Suppress the enclosing root key

Input / fixture:

```text
<order><id>SO1001</id></order>
```

Expected checks: {"jsonRoot": "id", "absentRoot": "order"}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
{"id":"SO1001"}
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 15

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XJ04 — Force all leaf elements into arrays

Input / fixture:

```text
<order><item>A1</item><qty>2</qty></order>
```

Expected checks: {"contains": ["A1", "2"]}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
{"order":[{"item":["A1"],"qty":["2"]}]}
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 39

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XJ05 — Keep a one-item field as an array

Input / fixture:

```text
<order><item>A1</item></order>
```

Expected checks: {"arrayPath": ["order", "item"]}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
{"order":{"item":["A1"]}}
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 25

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XJ06 — Streaming conversion with repeated items

Input / fixture:

```text
<order><item>A1</item><item>B2</item></order>
```

Expected checks: {"contains": ["A1", "B2"]}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
{"order":[{"item":["A1","B2"]}]}
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 32

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XJ07 — Map XML namespaces to colon JSON keys

Input / fixture:

```text
<p:order xmlns:p="urn:training:orders"><p:id>SO1003</p:id></p:order>
```

Expected checks: {"jsonRoot": "p:order"}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
{"p:order":{"p:id":"SO1003"}}
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 29

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XJ08 — UTF-16LE output bytes

Input / fixture:

```text
<customer><name>José సాయి</name></customer>
```

Expected checks: {"encoding": "UTF-16LE", "jsonRoot": "customer", "contains": ["José", "సాయి"]}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
{"customer":{"name":"José సాయి"}}
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 66

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XJ09 — Empty elements and boolean-looking text

Input / fixture:

```text
<order><note/><active>true</active></order>
```

Expected checks: {"jsonRoot": "order"}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
{"order":{"note":"","active":"true"}}
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 37

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XJ10 — Malformed XML is rejected

Input / fixture:

```text
<order><id>1</order>
```

Expected checks: {"negative": true}

Recorded: PILOT_SIMULATION / EXPECTED_REJECTION

```text
Unexpected close tag </order>; expected </id>.
 at [row,col {unknown-source}]: [1,19] on the exchange: Exchange[C2550689EC75F34-0000000000000001]
```

Native output:

```text
[empty body]
```

Java type: ; bytes: 

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XJ11 — Dot JSON namespace separator

Input / fixture:

```text
<p:order xmlns:p="urn:training:orders"><p:id>SO1004</p:id></p:order>
```

Expected checks: {"jsonRoot": "p.order"}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
{"p.order":{"p.id":"SO1004"}}
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 29

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### XML To JSON Converter

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Use Namespace Mapping | false | Use namespace mapping to define a relation between the JSON prefix and the XML namespace | When applicable |
| Processing | Namespace Mapping | [] | Enter the mapping of XML namespace to JSON Prefix | Conditional |
| Processing | XML Namespace |  |  | When applicable |
| Processing | JSON Prefix |  |  | When applicable |
| Processing | JSON Prefix Separator | : | Colon(:), Comma(,), Dot(.), Pipe(\|), Semicolon(;), Space | When applicable |
| Processing | JSON Output Encoding |  | From Header or Property, UTF-8, UTF-16BE, UTF-16LE, UTF-32BE, UTF-32LE | When applicable |
| Processing | Suppress JSON Root Element | false | Select this option to create the JSON document without root element | When applicable |
| Processing | Streaming | false | Select this option to enable streaming for large XML documents | When applicable |
| Processing | Convert XML Elements to JSON Array | specific | All, Specified Ones | Conditional |
| Processing | xmlJsonPathTable | [] | Specify the XML elements to be converted to JSON arrays. To do this, enter their absolute paths. All XML elements which have an occurrence greater than one must be specified.<br>Example: ns1:A/ns2:B/C<br>Th … [302 characters; full value in scenario/evidence JSON] | Conditional |
| Processing | XML Element |  |  | When applicable |
| Processing | fromXsd |  |  | Conditional |

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | constant | Expression, Constant | When applicable |
| Message Body | Body | <customer><name>Anu</name></customer> | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: [{"severity": "Warning", "message": "Content Modifier may not pass XML message to XML To JSON Converter. XML To JSON Converter supports XML input only.", "elementId": "CallActivity_11", "elementType": "CallActivity", "location": "XmlToJsonConverter", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 003 - XML to JSON.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}]

# TRN 004 - XPath Filter

Technical ID: IF_TRN_004_XPath_Filter. Version 2.0.1.

Default Configure → ScenarioId=XP01. Save and deploy once.

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| XP01 | Extract customer name | string | PILOT_SIMULATION: PASS |
| XP02 | XPath count with Integer setting: runtime numeric rendering 3.0 | integer | PILOT_SIMULATION: PASS |
| XP03 | High-value order true | boolean | PILOT_SIMULATION: PASS |
| XP04 | High-value order false | boolean | PILOT_SIMULATION: PASS |
| XP05 | Select only the first item node | node | PILOT_SIMULATION: PASS |
| XP06 | Select all item nodes | nodes | PILOT_SIMULATION: PASS |
| XP07 | Missing optional value becomes empty text | missing | PILOT_SIMULATION: PASS |
| XP08 | Runtime Configuration namespace prefix | ns | PILOT_SIMULATION: PASS |
| XP09 | Predicate selects quantity over one | predicate | PILOT_SIMULATION: PASS |
| XP10 | Malformed input XML is rejected | string | PILOT_SIMULATION: EXPECTED_REJECTION |

## XP01 — Extract customer name

Input / fixture:

```text
<order><customer><name>Anu</name></customer></order>
```

Expected checks: {"exact": "Anu"}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
Anu
```

Java type: java.lang.String; bytes: 3

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XP02 — XPath count with Integer setting: runtime numeric rendering 3.0

Input / fixture:

```text
<order><items><item/><item/><item/></items></order>
```

Expected checks: {"exact": "3.0"}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
3.0
```

Java type: java.lang.Double; bytes: 3

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XP03 — High-value order true

Input / fixture:

```text
<order><amount>1500</amount></order>
```

Expected checks: {"exact": "true"}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
true
```

Java type: java.lang.Boolean; bytes: 4

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XP04 — High-value order false

Input / fixture:

```text
<order><amount>500</amount></order>
```

Expected checks: {"exact": "false"}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
false
```

Java type: java.lang.Boolean; bytes: 5

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XP05 — Select only the first item node

Input / fixture:

```text
<order><items><item><sku>A1</sku></item><item><sku>B2</sku></item></items></order>
```

Expected checks: {"contains": ["A1"], "excludes": ["B2"]}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
<item><sku>A1</sku></item>
```

Java type: net.sf.saxon.dom.ElementOverNodeInfo; bytes: 26

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XP06 — Select all item nodes

Input / fixture:

```text
<order><items><item>A1</item><item>B2</item></items></order>
```

Expected checks: {"contains": ["A1", "B2"]}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
<item>A1</item><item>B2</item>
```

Java type: net.sf.saxon.dom.DOMNodeList; bytes: 30

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XP07 — Missing optional value becomes empty text

Input / fixture:

```text
<order><customer><name>Anu</name></customer></order>
```

Expected checks: {"exact": ""}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
[empty body]
```

Java type: java.lang.String; bytes: 0

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XP08 — Runtime Configuration namespace prefix

Input / fixture:

```text
<o:order xmlns:o="urn:training:orders"><o:id>SO1003</o:id></o:order>
```

Expected checks: {"exact": "SO1003"}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
SO1003
```

Java type: java.lang.String; bytes: 6

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XP09 — Predicate selects quantity over one

Input / fixture:

```text
<order><items><item><sku>A1</sku><qty>2</qty></item><item><sku>B2</sku><qty>1</qty></item></items></order>
```

Expected checks: {"contains": ["A1"], "excludes": ["B2"]}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
<item><sku>A1</sku><qty>2</qty></item>
```

Java type: net.sf.saxon.dom.DOMNodeList; bytes: 38

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XP10 — Malformed input XML is rejected

Input / fixture:

```text
<order>
```

Expected checks: {"negative": true}

Recorded: PILOT_SIMULATION / EXPECTED_REJECTION

```text
javax.xml.xpath.XPathExpressionException: Failure converting a node of class javax.xml.transform.sax.SAXSource: org.xml.sax.SAXParseException; lineNumber: 1; columnNumber: 8; XML document structures must start and end within the same entity. on the exchange: Exchange[88DAB3D80302896-0000000000000001]
```

Native output:

```text
[empty body]
```

Java type: ; bytes: 

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Filter

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | XPath Expression | /order/id/text() | Enter an XPath to extract a node in the message.<br>E.g.<br>/ns0:MessageBulk/Message/MessageContent/text(),<br>//Message/orders | When applicable |
| Processing | Value Type | String | String, Integer, Boolean, Node, Nodelist | When applicable |

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | constant | Expression, Constant | When applicable |
| Message Body | Body | <order><id>1001</id><amount>250</amount></order> | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: [{"severity": "Warning", "message": "Content Modifier may not pass XML message to Filter. Filter supports XML input only.", "elementId": "CallActivity_11", "elementType": "CallActivity", "attributeId": "iflow.advance.cfilter.tab", "location": "Filter", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 004 - XPath Filter.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}]

# TRN 005 - Base64 Encode

Technical ID: IF_TRN_005_Base64_Encode. Version 2.0.1.

Default Configure → ScenarioId=BE01. Save and deploy once.

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| BE01 | Plain text | standard | PILOT_SIMULATION: PASS |
| BE02 | Encode a business JSON payload | standard | PILOT_SIMULATION: PASS |
| BE03 | Encode XML exactly, without XML parsing | standard | PILOT_SIMULATION: PASS |
| BE04 | UTF-8 Unicode bytes | standard | PILOT_SIMULATION: PASS |
| BE05 | Preserve line-ending bytes | standard | PILOT_SIMULATION: PASS |
| BE06 | Empty payload | standard | PILOT_SIMULATION: PASS |
| BE07 | Binary file bytes including NUL | standard | PILOT_SIMULATION: PASS |
| BE08 | MIME line wrapping and large-enough payload | standard | PILOT_SIMULATION: PASS |

## BE01 — Plain text

Input / fixture:

```text
Hello
```

Expected checks: {"base64Of": "Hello", "expectedBase64": "SGVsbG8="}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
SGVsbG8=\u000d

```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 10

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## BE02 — Encode a business JSON payload

Input / fixture:

```text
{"orderId":"SO1001","amount":250}
```

Expected checks: {"base64Of": "{\"orderId\":\"SO1001\",\"amount\":250}", "expectedBase64": "eyJvcmRlcklkIjoiU08xMDAxIiwiYW1vdW50IjoyNTB9"}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
eyJvcmRlcklkIjoiU08xMDAxIiwiYW1vdW50IjoyNTB9\u000d

```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 46

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## BE03 — Encode XML exactly, without XML parsing

Input / fixture:

```text
<order id="SO1001">A &amp; B</order>
```

Expected checks: {"base64Of": "<order id=\"SO1001\">A &amp; B</order>", "expectedBase64": "PG9yZGVyIGlkPSJTTzEwMDEiPkEgJmFtcDsgQjwvb3JkZXI+"}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
PG9yZGVyIGlkPSJTTzEwMDEiPkEgJmFtcDsgQjwvb3JkZXI+\u000d

```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 50

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## BE04 — UTF-8 Unicode bytes

Input / fixture:

```text
José సాయి 😀
```

Expected checks: {"base64Of": "José సాయి 😀", "expectedBase64": "Sm9zw6kg4LC44LC+4LCv4LC/IPCfmIA="}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
Sm9zw6kg4LC44LC+4LCv4LC/IPCfmIA=\u000d

```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 34

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## BE05 — Preserve line-ending bytes

Input / fixture:

```text
line1
line2\u000d
line3
```

Expected checks: {"base64Of": "line1\nline2\r\nline3", "expectedBase64": "bGluZTEKbGluZTINCmxpbmUz"}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
bGluZTEKbGluZTINCmxpbmUz\u000d

```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 26

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## BE06 — Empty payload

Input / fixture:

```text
[empty body]
```

Expected checks: {"base64Of": "", "expectedBase64": ""}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
[empty body]
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 0

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## BE07 — Binary file bytes including NUL

Input / fixture:

```text
AAEC/w0K
```

Expected checks: {"expectedBase64": "AAEC/w0K"}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
AAEC/w0K\u000d

```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 10

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## BE08 — MIME line wrapping and large-enough payload

Input / fixture:

```text
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
```

Expected checks: {"base64Of": "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx", "expectedBase64": "eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4"}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4\u000d
eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4\u000d
eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4\u000d

```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 206

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Base64 Encode

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | constant | Expression, Constant | When applicable |
| Message Body | Body | Hello | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: []

# TRN 006 - Base64 Decode

Technical ID: IF_TRN_006_Base64_Decode. Version 2.0.1.

Default Configure → ScenarioId=BD01. Save and deploy once.

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| BD01 | Decode text | standard | PILOT_SIMULATION: PASS |
| BD02 | Decode UTF-8 Unicode | standard | PILOT_SIMULATION: PASS |
| BD03 | Restore line-ending bytes | standard | PILOT_SIMULATION: PASS |
| BD04 | Empty payload | standard | PILOT_SIMULATION: PASS |
| BD05 | MIME-wrapped input | standard | PILOT_SIMULATION: PASS |
| BD06 | Binary file bytes including NUL | standard | PILOT_SIMULATION: PASS |
| BD07 | Missing padding behaviour | standard | PILOT_SIMULATION: PASS |
| BD08 | Invalid alphabet: measure actual decoder behaviour | standard | PILOT_SIMULATION: OBSERVED |
| BD09 | Invalid single-character input: decoder permissiveness | standard | PILOT_SIMULATION: OBSERVED |
| BD10 | Reject invalid Base64 with a strict input guard | standard | PILOT_SIMULATION: EXPECTED_REJECTION |
| BD11 | Strict input guard accepts valid Base64 | standard | PILOT_SIMULATION: PASS |

## BD01 — Decode text

Input / fixture:

```text
SGVsbG8=
```

Expected checks: {"exact": "Hello"}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
Hello
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 5

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## BD02 — Decode UTF-8 Unicode

Input / fixture:

```text
Sm9zw6kg4LC44LC+4LCv4LC/IPCfmIA=
```

Expected checks: {"exact": "José సాయి 😀"}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
José సాయి 😀
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 23

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## BD03 — Restore line-ending bytes

Input / fixture:

```text
bGluZTEKbGluZTINCmxpbmUz
```

Expected checks: {"exact": "line1\nline2\r\nline3"}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
line1
line2\u000d
line3
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 18

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## BD04 — Empty payload

Input / fixture:

```text
[empty body]
```

Expected checks: {"exact": ""}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
[empty body]
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 0

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## BD05 — MIME-wrapped input

Input / fixture:

```text
SGVs\u000d
bG8=
```

Expected checks: {"exact": "Hello"}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
Hello
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 5

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## BD06 — Binary file bytes including NUL

Input / fixture:

```text
AAEC/w0K
```

Expected checks: {"expectedBinary": "AAEC/w0K"}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
\u0000\u0001\u0002�\u000d

```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 6

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## BD07 — Missing padding behaviour

Input / fixture:

```text
SGVsbG8
```

Expected checks: {"exact": "Hello"}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
Hello
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 5

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## BD08 — Invalid alphabet: measure actual decoder behaviour

Input / fixture:

```text
%%%not-base64%%%
```

Expected checks: {"observe": true}

Recorded: PILOT_SIMULATION / OBSERVED

Native output:

```text
��~m�\u001e�
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 7

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## BD09 — Invalid single-character input: decoder permissiveness

Input / fixture:

```text
A
```

Expected checks: {"observe": true, "exact": ""}

Recorded: PILOT_SIMULATION / OBSERVED

Native output:

```text
[empty body]
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 0

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## BD10 — Reject invalid Base64 with a strict input guard

Input / fixture:

```text
%%%not-base64%%%
```

Expected checks: {"strict": true, "negative": true, "failureStage": "Load Scenario Fixture input guard"}

Recorded: PILOT_SIMULATION / EXPECTED_REJECTION

```text
java.lang.Exception: java.lang.IllegalArgumentException: Illegal base64 character 25@ line 8 in LoadScenario.groovy on the exchange: Exchange[C6406D43C754A20-0000000000000001]
```

Native output:

```text
[empty body]
```

Java type: ; bytes: 

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## BD11 — Strict input guard accepts valid Base64

Input / fixture:

```text
SGVsbG8=
```

Expected checks: {"strict": true, "exact": "Hello"}

Recorded: PILOT_SIMULATION / PASS

Native output:

```text
Hello
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 5

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Base64 Decode

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | constant | Expression, Constant | When applicable |
| Message Body | Body | SGVsbG8= | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: [{"severity": "Warning", "message": "Content Modifier may not pass Base64Encoded message to Decoder/Base64 Decode. Decoder/Base64 Decode supports Base64Encoded input only.", "elementId": "CallActivity_11", "elementType": "CallActivity", "attributeId": "iflow.advance.decoder.tab", "location": "Base64 Decode", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 006 - Base64 Decode.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}]

# TRN 007 - GZIP Roundtrip

Technical ID: IF_TRN_007_GZIP_Roundtrip. Version 3.0.0.

Default Configure → ScenarioId=L007_1. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| L007_1 | Compression/decompression round trip — business text | base | SIMULATION: PASS |
| L007_2 | Compression/decompression round trip — UTF-8 Unicode | base | SIMULATION: PASS |
| L007_3 | Compression/decompression round trip — line endings | base | SIMULATION: PASS |
| L007_4 | Compression/decompression round trip — empty body | base | SIMULATION: PASS |
| L007_5 | Compression/decompression round trip — repetitive larger payload | base | SIMULATION: PASS |
| L007_BAD | Corrupted archive rejected before decompression | base | SIMULATION: EXPECTED_REJECTION |

## L007_1 — Compression/decompression round trip — business text

Input / fixture:

```text
Order SO1001, amount 250
```

Expected checks: {"exact": "Order SO1001, amount 250"}

Recorded: SIMULATION / PASS

Native output:

```text
Order SO1001, amount 250
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 24

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## L007_2 — Compression/decompression round trip — UTF-8 Unicode

Input / fixture:

```text
José సాయి 😀
```

Expected checks: {"exact": "José సాయి 😀"}

Recorded: SIMULATION / PASS

Native output:

```text
José సాయి 😀
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 23

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## L007_3 — Compression/decompression round trip — line endings

Input / fixture:

```text
line1
line2\u000d
line3
```

Expected checks: {"exact": "line1\nline2\r\nline3"}

Recorded: SIMULATION / PASS

Native output:

```text
line1
line2\u000d
line3
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 18

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## L007_4 — Compression/decompression round trip — empty body

Input / fixture:

```text
[empty body]
```

Expected checks: {"exact": ""}

Recorded: SIMULATION / PASS

Native output:

```text
[empty body]
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 0

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## L007_5 — Compression/decompression round trip — repetitive larger payload

Input / fixture:

```text
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx … [4096 characters; full value in scenario/evidence JSON]
```

Expected checks: {"exact": "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx … [4109 characters; full value in scenario/evidence JSON]

Recorded: SIMULATION / PASS

Native output:

```text
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx … [4096 characters; full value in scenario/evidence JSON]
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 4096

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## L007_BAD — Corrupted archive rejected before decompression

Input / fixture:

```text
A valid source payload
```

Expected checks: {"corrupt": true, "negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
Input is not in the .gz format. on the exchange: Exchange[18DA5B180C588AB-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### GZIP Decompress

No additional component processing fields; inspect General and the surrounding message flow.

### GZIP Compress

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [807 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: [{"severity": "Warning", "message": "Script may not pass GZip message to Decoder/GZIP Decompress. Decoder/GZIP Decompress supports GZip input only.", "elementId": "CallActivity_102", "elementType": "CallActivity", "attributeId": "iflow.advance.decoder.tab", "location": "base - GZIP Decompress", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 007 - GZIP Roundtrip.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}]

# TRN 008 - ZIP Roundtrip

Technical ID: IF_TRN_008_ZIP_Roundtrip. Version 3.0.0.

Default Configure → ScenarioId=L008_1. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| L008_1 | Compression/decompression round trip — business text | base | SIMULATION: PASS |
| L008_2 | Compression/decompression round trip — UTF-8 Unicode | base | SIMULATION: PASS |
| L008_3 | Compression/decompression round trip — line endings | base | SIMULATION: PASS |
| L008_4 | Compression/decompression round trip — empty body | base | SIMULATION: PASS |
| L008_5 | Compression/decompression round trip — repetitive larger payload | base | SIMULATION: PASS |
| L008_BAD | Corrupted archive rejected before decompression | base | SIMULATION: EXPECTED_REJECTION |

## L008_1 — Compression/decompression round trip — business text

Input / fixture:

```text
Order SO1001, amount 250
```

Expected checks: {"exact": "Order SO1001, amount 250"}

Recorded: SIMULATION / PASS

Native output:

```text
Order SO1001, amount 250
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 24

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## L008_2 — Compression/decompression round trip — UTF-8 Unicode

Input / fixture:

```text
José సాయి 😀
```

Expected checks: {"exact": "José సాయి 😀"}

Recorded: SIMULATION / PASS

Native output:

```text
José సాయి 😀
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 23

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## L008_3 — Compression/decompression round trip — line endings

Input / fixture:

```text
line1
line2\u000d
line3
```

Expected checks: {"exact": "line1\nline2\r\nline3"}

Recorded: SIMULATION / PASS

Native output:

```text
line1
line2\u000d
line3
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 18

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## L008_4 — Compression/decompression round trip — empty body

Input / fixture:

```text
[empty body]
```

Expected checks: {"exact": ""}

Recorded: SIMULATION / PASS

Native output:

```text
[empty body]
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 0

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## L008_5 — Compression/decompression round trip — repetitive larger payload

Input / fixture:

```text
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx … [4096 characters; full value in scenario/evidence JSON]
```

Expected checks: {"exact": "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx … [4109 characters; full value in scenario/evidence JSON]

Recorded: SIMULATION / PASS

Native output:

```text
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx … [4096 characters; full value in scenario/evidence JSON]
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 4096

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## L008_BAD — Corrupted archive rejected before decompression

Input / fixture:

```text
A valid source payload
```

Expected checks: {"corrupt": true, "negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
Cannot find zip signature within the file on the exchange: Exchange[D0900861F31C45E-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### ZIP Decompress

No additional component processing fields; inspect General and the surrounding message flow.

### ZIP Compress

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [807 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: [{"severity": "Warning", "message": "Script may not pass Zip message to Decoder/ZIP Decompress. Decoder/ZIP Decompress supports Zip input only.", "elementId": "CallActivity_102", "elementType": "CallActivity", "attributeId": "iflow.advance.decoder.tab", "location": "base - ZIP Decompress", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 008 - ZIP Roundtrip.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}]

# TRN 009 - XML Declaration

Technical ID: IF_TRN_009_XML_Declaration. Version 3.0.0.

Default Configure → ScenarioId=XML09_REMOVE. Save and deploy once.

Configuration branches and overrides: {"base": {}, "keep": {"overrides": {"0": {"removeXmlDeclaration": "0"}}}, "clean": {"overrides": {"0": {"xmlCharacterHandling": "remove"}}}, "substitute": {"overrides": {"0": {"xmlCharacterHandling": "substitute"}}}, "dtd": {"overrides": {"0": {"removeExternalDTDs": "1"}}}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| XML09_REMOVE | Remove declaration | base | SIMULATION: PASS |
| XML09_KEEP | Keep declaration | keep | SIMULATION: PASS |
| XML09_UNICODE | Preserve Unicode and valid XML | base | SIMULATION: PASS |
| XML09_CONTROL | Remove invalid XML control character | clean | SIMULATION: PASS |
| XML09_SUB | Substitute invalid XML control character | substitute | SIMULATION: PASS |
| XML09_DTD | Remove external DTD without fetching it | dtd | SIMULATION: PASS |

## XML09_REMOVE — Remove declaration

Input / fixture:

```text
<?xml version="1.0"?><student><name>Anu</name></student>
```

Expected checks: {"exact": "<student><name>Anu</name></student>"}

Recorded: SIMULATION / PASS

Native output:

```text
<student><name>Anu</name></student>
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 35

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XML09_KEEP — Keep declaration

Input / fixture:

```text
<?xml version="1.0"?><student><name>Anu</name></student>
```

Expected checks: {"contains": ["<?xml", "Anu"]}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version="1.0"?><student><name>Anu</name></student>
```

Java type: java.lang.String; bytes: 56

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XML09_UNICODE — Preserve Unicode and valid XML

Input / fixture:

```text
<student><name>José సాయి</name></student>
```

Expected checks: {"xmlRoot": "student", "contains": ["José సాయి"]}

Recorded: SIMULATION / PASS

Native output:

```text
<student><name>José సాయి</name></student>
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 50

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XML09_CONTROL — Remove invalid XML control character

Input / fixture:

```text
<student>A\u0001B</student>
```

Expected checks: {"exact": "<student>AB</student>"}

Recorded: SIMULATION / PASS

Native output:

```text
<student>AB</student>
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 21

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XML09_SUB — Substitute invalid XML control character

Input / fixture:

```text
<student>A\u0001B</student>
```

Expected checks: {"contains": ["A", "B"], "excludes": ["\u0001"]}

Recorded: SIMULATION / PASS

Native output:

```text
<student>A#B</student>
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 22

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XML09_DTD — Remove external DTD without fetching it

Input / fixture:

```text
<!DOCTYPE student SYSTEM "urn:training:never-fetch"><student>Anu</student>
```

Expected checks: {"contains": ["Anu"], "excludes": ["DOCTYPE"]}

Recorded: SIMULATION / PASS

Native output:

```text
<student>Anu</student>
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 22

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### XML Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Remove XML Declaration | 1 | Select to remove the XML declaration from the XML payload | When applicable |
| Processing | Remove External DTDs | 0 | Select to remove DTDs with external references from the XML payload | When applicable |
| Processing | Invalid XML Character Handling | none | None, Remove, Substitute | When applicable |

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/LoadLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [819 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

### Router

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Throw Exception | false | Exception is thrown only if the sender channel is either IDoc or SOAP adapter, and the default route contains only Terminate End Event. | When applicable |
| Processing | conditionTable | [{"branchId": {"value": "SequenceFlow_200", "defaultValue": "", "key": ""}, "defaultBranch": {"value": "false", "defaultValue": "", "key": ""}, "conditionValue" … [2060 characters; full value in scenario/evidence JSON] |  | When applicable |
| Processing | Order |  |  | When applicable |
| Processing | Route Name |  |  | When applicable |
| Processing | Condition Expression |  |  | When applicable |
| Processing | Default Route |  |  | When applicable |

Saved design messages: [{"severity": "Warning", "message": "Script may not pass XML message to XML Modifier. XML Modifier supports XML input only.", "elementId": "CallActivity_190", "elementType": "CallActivity", "location": "substitute - XML Modifier", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 009 - XML Declaration.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}, {"severity": "Warning", "message": "Script may not pass XML message to XML Modifier. XML Modifier supports XML input only.", "elementId": "CallActivity_130", "elementType": "CallActivity", "location": "keep - XML Modifier", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 009 - XML Declaration.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}, {"severity": "Warning", "message": "Script may not pass XML message to XML Modifier. XML Modifier supports XML input only.", "elementId": "CallActivity_160", "elementType": "CallActivity", "loc … [2006 characters; full value in scenario/evidence JSON]

# TRN 010 - XML to CSV

Technical ID: IF_TRN_010_XML_to_CSV. Version 3.0.0.

Default Configure → ScenarioId=CSV10_ROWS. Save and deploy once.

Configuration branches and overrides: {"base": {}, "semi": {"overrides": {"0": {"Field_Separator_in_CSV": ";"}}}, "pipe": {"overrides": {"0": {"Field_Separator_in_CSV": "|"}}}, "header": {"overrides": {"0": {"CSV_Header": "xml"}}}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| CSV10_ROWS | Two records to comma CSV | base | SIMULATION: PASS |
| CSV10_UNICODE | Unicode data | base | SIMULATION: PASS |
| CSV10_SEMI | Semicolon separator | semi | SIMULATION: PASS |
| CSV10_PIPE | Pipe separator | pipe | SIMULATION: PASS |
| CSV10_HEADER | XML element names as header | header | SIMULATION: PASS |
| CSV10_QUOTE | Field containing separator is quoted | base | SIMULATION: PASS |
| CSV10_BAD | Malformed XML rejected | base | SIMULATION: EXPECTED_REJECTION |

## CSV10_ROWS — Two records to comma CSV

Input / fixture:

```text
<students><student><id>1</id><name>Anu</name></student><student><id>2</id><name>Ravi</name></student></students>
```

Expected checks: {"normalizedLines": "1,Anu\n2,Ravi"}

Recorded: SIMULATION / PASS

Native output:

```text
1,Anu\u000d
2,Ravi\u000d

```

Java type: java.io.ByteArrayOutputStream; bytes: 15

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## CSV10_UNICODE — Unicode data

Input / fixture:

```text
<students><student><id>3</id><name>José సాయి</name></student></students>
```

Expected checks: {"normalizedLines": "3,José సాయి"}

Recorded: SIMULATION / PASS

Native output:

```text
3,José సాయి\u000d

```

Java type: java.io.ByteArrayOutputStream; bytes: 22

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## CSV10_SEMI — Semicolon separator

Input / fixture:

```text
<students><student><id>1</id><name>Anu</name></student><student><id>2</id><name>Ravi</name></student></students>
```

Expected checks: {"normalizedLines": "1;Anu\n2;Ravi"}

Recorded: SIMULATION / PASS

Native output:

```text
1;Anu\u000d
2;Ravi\u000d

```

Java type: java.io.ByteArrayOutputStream; bytes: 15

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## CSV10_PIPE — Pipe separator

Input / fixture:

```text
<students><student><id>1</id><name>Anu</name></student><student><id>2</id><name>Ravi</name></student></students>
```

Expected checks: {"normalizedLines": "1|Anu\n2|Ravi"}

Recorded: SIMULATION / PASS

Native output:

```text
1|Anu\u000d
2|Ravi\u000d

```

Java type: java.io.ByteArrayOutputStream; bytes: 15

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## CSV10_HEADER — XML element names as header

Input / fixture:

```text
<students><student><id>1</id><name>Anu</name></student><student><id>2</id><name>Ravi</name></student></students>
```

Expected checks: {"contains": ["id,name", "1,Anu", "2,Ravi"]}

Recorded: SIMULATION / PASS

Native output:

```text
id,name\u000d
1,Anu\u000d
2,Ravi\u000d

```

Java type: java.io.ByteArrayOutputStream; bytes: 24

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## CSV10_QUOTE — Field containing separator is quoted

Input / fixture:

```text
<students><student><id>4</id><name>Anu, Ravi</name></student></students>
```

Expected checks: {"contains": ["Anu, Ravi"]}

Recorded: SIMULATION / PASS

Native output:

```text
4,"Anu, Ravi"\u000d

```

Java type: java.io.ByteArrayOutputStream; bytes: 15

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## CSV10_BAD — Malformed XML rejected

Input / fixture:

```text
<students><student>
```

Expected checks: {"negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
Unmarshalling XML failed on the exchange: Exchange[B4245AD67A12DD7-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### Router

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Throw Exception | false | Exception is thrown only if the sender channel is either IDoc or SOAP adapter, and the default route contains only Terminate End Event. | When applicable |
| Processing | conditionTable | [{"branchId": {"value": "SequenceFlow_200", "defaultValue": "", "key": ""}, "defaultBranch": {"value": "false", "defaultValue": "", "key": ""}, "conditionValue" … [1621 characters; full value in scenario/evidence JSON] |  | When applicable |
| Processing | Order |  |  | When applicable |
| Processing | Route Name |  |  | When applicable |
| Processing | Condition Expression |  |  | When applicable |
| Processing | Default Route |  |  | When applicable |

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### XML To CSV Converter

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Path to Source Element in XML | /students/student | Enter the XPath of the Record Identifier | When applicable |
| Processing | Field Separator in CSV | ; | Tab(\t), Semicolon(;), Colon(:), Pipe(\|), Dot(.), Comma(,), Space | When applicable |
| Processing | CSV Header | none | None, XSD annotation based, XML based | When applicable |
| Processing | XML Schema |  | Enter $(header.xsdPath) or $(property.xsdPath) to read the path dynamically from a header or a property. | Conditional |
| Processing | Include Parent Element | false | Include master node for conversion | When applicable |
| Processing | Path to Parent Element |  | Enter the XPath of the Master Record Identifier | Conditional |
| Processing | Include Attribute Values | false | Include attribute values | When applicable |

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [815 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: [{"severity": "Warning", "message": "Script may not pass XML message to XML To CSV Converter. XML To CSV Converter supports XML input only.", "elementId": "CallActivity_100", "elementType": "CallActivity", "location": "base - XML To CSV Converter", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 010 - XML to CSV.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}, {"severity": "Warning", "message": "Script may not pass XML message to XML To CSV Converter. XML To CSV Converter supports XML input only.", "elementId": "CallActivity_160", "elementType": "CallActivity", "location": "pipe - XML To CSV Converter", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 010 - XML to CSV.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}, {"severity": "Warning", "message": "Script may not pass XML message to XML To CSV Converter. XML To CSV Converter supports XML input only.", "elementId": "CallAc … [1678 characters; full value in scenario/evidence JSON]

# TRN 011 - XML JSON Roundtrip

Technical ID: IF_TRN_011_XML_JSON_Roundtrip. Version 3.0.0.

Default Configure → ScenarioId=RT11_ORDER. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| RT11_ORDER | XML→JSON→XML preserves values | base | SIMULATION: PASS |
| RT11_REPEAT | Repeated elements survive round trip | base | SIMULATION: PASS |
| RT11_UNICODE | Unicode survives round trip | base | SIMULATION: PASS |
| RT11_EMPTY | Empty element representation | base | SIMULATION: PASS |
| RT11_BAD | Malformed XML rejected | base | SIMULATION: EXPECTED_REJECTION |

## RT11_ORDER — XML→JSON→XML preserves values

Input / fixture:

```text
<customer><name>Anu</name><amount>250</amount></customer>
```

Expected checks: {"xmlEquivalent": "<customer><name>Anu</name><amount>250</amount></customer>"}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><customer><name>Anu</name><amount>250</amount></customer>
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 95

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## RT11_REPEAT — Repeated elements survive round trip

Input / fixture:

```text
<customer><name>Anu</name><item>A1</item><item>B2</item></customer>
```

Expected checks: {"xmlLeafValues": ["Anu", "A1", "B2"]}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><customer><name>Anu</name><item>A1</item><item>B2</item></customer>
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 105

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## RT11_UNICODE — Unicode survives round trip

Input / fixture:

```text
<customer><name>José సాయి</name></customer>
```

Expected checks: {"xmlLeafValues": ["José సాయి"]}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><customer><name>José సాయి</name></customer>
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 90

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## RT11_EMPTY — Empty element representation

Input / fixture:

```text
<customer><name/></customer>
```

Expected checks: {"xmlRoot": "customer"}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><customer><name></name></customer>
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 72

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## RT11_BAD — Malformed XML rejected

Input / fixture:

```text
<customer>
```

Expected checks: {"negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
Unexpected EOF; was expecting a close tag for element <customer>
 at [row,col {unknown-source}]: [1,10] on the exchange: Exchange[797CD8AAACECFC0-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### JSON To XML Converter

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Use Namespace Mapping | false | Use namespace mapping to define a relation between the JSON prefix and the XML namespace | When applicable |
| Processing | Namespace Mapping | [] | Enter the mapping of the JSON prefix to the XML namespace | Conditional |
| Processing | JSON Prefix |  |  | When applicable |
| Processing | XML Namespace |  |  | When applicable |
| Processing | JSON Prefix Separator | : | Colon(:), Comma(,), Dot(.), Pipe(\|), Semicolon(;), Space | When applicable |
| Processing | Add XML Root Element | false | Add a root element to the produced document | When applicable |
| Processing | Name | root | Enter the name of the root element | Conditional |
| Processing | Namespace Mapping |  | Select the namespace mapping for the root element | Conditional |

### XML To JSON Converter

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Use Namespace Mapping | false | Use namespace mapping to define a relation between the JSON prefix and the XML namespace | When applicable |
| Processing | Namespace Mapping | [] | Enter the mapping of XML namespace to JSON Prefix | Conditional |
| Processing | XML Namespace |  |  | When applicable |
| Processing | JSON Prefix |  |  | When applicable |
| Processing | JSON Prefix Separator | : | Colon(:), Comma(,), Dot(.), Pipe(\|), Semicolon(;), Space | When applicable |
| Processing | JSON Output Encoding |  | From Header or Property, UTF-8, UTF-16BE, UTF-16LE, UTF-32BE, UTF-32LE | When applicable |
| Processing | Suppress JSON Root Element | false | Select this option to create the JSON document without root element | When applicable |
| Processing | Streaming | false | Select this option to enable streaming for large XML documents | When applicable |
| Processing | Convert XML Elements to JSON Array | specific | All, Specified Ones | Conditional |
| Processing | xmlJsonPathTable | [] | Specify the XML elements to be converted to JSON arrays. To do this, enter their absolute paths. All XML elements which have an occurrence greater than one must be specified.<br>Example: ns1:A/ns2:B/C<br>Th … [302 characters; full value in scenario/evidence JSON] | Conditional |
| Processing | XML Element |  |  | When applicable |
| Processing | fromXsd |  |  | Conditional |

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [815 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: [{"severity": "Warning", "message": "Script may not pass XML message to XML To JSON Converter. XML To JSON Converter supports XML input only.", "elementId": "CallActivity_100", "elementType": "CallActivity", "location": "base - XML To JSON Converter", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 011 - XML JSON Roundtrip.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}]

# TRN 012 - Base64 Roundtrip

Technical ID: IF_TRN_012_Base64_Roundtrip. Version 3.0.0.

Default Configure → ScenarioId=L012_1. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| L012_1 | Base64 encode/decode round trip — business text | base | SIMULATION: PASS |
| L012_2 | Base64 encode/decode round trip — UTF-8 Unicode | base | SIMULATION: PASS |
| L012_3 | Base64 encode/decode round trip — line endings | base | SIMULATION: PASS |
| L012_4 | Base64 encode/decode round trip — empty body | base | SIMULATION: PASS |
| L012_5 | Base64 encode/decode round trip — repetitive larger payload | base | SIMULATION: PASS |

## L012_1 — Base64 encode/decode round trip — business text

Input / fixture:

```text
Order SO1001, amount 250
```

Expected checks: {"exact": "Order SO1001, amount 250"}

Recorded: SIMULATION / PASS

Native output:

```text
Order SO1001, amount 250
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 24

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## L012_2 — Base64 encode/decode round trip — UTF-8 Unicode

Input / fixture:

```text
José సాయి 😀
```

Expected checks: {"exact": "José సాయి 😀"}

Recorded: SIMULATION / PASS

Native output:

```text
José సాయి 😀
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 23

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## L012_3 — Base64 encode/decode round trip — line endings

Input / fixture:

```text
line1
line2\u000d
line3
```

Expected checks: {"exact": "line1\nline2\r\nline3"}

Recorded: SIMULATION / PASS

Native output:

```text
line1
line2\u000d
line3
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 18

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## L012_4 — Base64 encode/decode round trip — empty body

Input / fixture:

```text
[empty body]
```

Expected checks: {"exact": ""}

Recorded: SIMULATION / PASS

Native output:

```text
[empty body]
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 0

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## L012_5 — Base64 encode/decode round trip — repetitive larger payload

Input / fixture:

```text
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx … [4096 characters; full value in scenario/evidence JSON]
```

Expected checks: {"exact": "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx … [4109 characters; full value in scenario/evidence JSON]

Recorded: SIMULATION / PASS

Native output:

```text
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx … [4096 characters; full value in scenario/evidence JSON]
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 4096

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Base64 Decode

No additional component processing fields; inspect General and the surrounding message flow.

### Base64 Encode

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [807 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: []

# TRN 013 - SHA256 Digest

Technical ID: IF_TRN_013_SHA256_Digest. Version 3.0.0.

Default Configure → ScenarioId=HASH13_SHA256. Save and deploy once.

Configuration branches and overrides: {"SHA-256": {"overrides": {"0": {"digestAlgorithm": "SHA-256"}}}, "SHA-384": {"overrides": {"0": {"digestAlgorithm": "SHA-384"}}}, "SHA-512": {"overrides": {"0": {"digestAlgorithm": "SHA-512"}}}, "SHA-1": {"overrides": {"0": {"digestAlgorithm": "SHA-1"}}}, "MD5": {"overrides": {"0": {"digestAlgorithm": "MD5"}}}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| HASH13_SHA256 | Digest SHA-256 | SHA-256 | SIMULATION: PASS |
| HASH13_SHA384 | Digest SHA-384 | SHA-384 | SIMULATION: PASS |
| HASH13_SHA512 | Digest SHA-512 | SHA-512 | SIMULATION: PASS |
| HASH13_SHA1 | Digest SHA-1 (legacy demonstration only) | SHA-1 | SIMULATION: PASS |
| HASH13_MD5 | Digest MD5 (legacy demonstration only) | MD5 | SIMULATION: PASS |
| HASH13_EDGE0 | SHA-256 exact UTF-8 bytes | SHA-256 | SIMULATION: PASS |
| HASH13_EDGE1 | SHA-256 exact UTF-8 bytes | SHA-256 | SIMULATION: PASS |
| HASH13_EDGE2 | SHA-256 exact UTF-8 bytes | SHA-256 | SIMULATION: PASS |

## HASH13_SHA256 — Digest SHA-256

Input / fixture:

```text
Order SO1001
```

Expected checks: {"headers": {"SAPMessageDigest": "pblmxSeVEKDDCJ6XBgVppoxSakIJFhScbIAcYhtShK4="}, "exact": "Order SO1001"}

Recorded: SIMULATION / PASS

Native output:

```text
Order SO1001
```

Java type: java.lang.String; bytes: 12

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## HASH13_SHA384 — Digest SHA-384

Input / fixture:

```text
Order SO1001
```

Expected checks: {"headers": {"SAPMessageDigest": "gwAj9eB/9teFnx5PdQPBPHcnCjFED1FGP4/QOmjg34JlKGg5p8JYrg0sMJ0tC3uk"}, "exact": "Order SO1001"}

Recorded: SIMULATION / PASS

Native output:

```text
Order SO1001
```

Java type: java.lang.String; bytes: 12

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## HASH13_SHA512 — Digest SHA-512

Input / fixture:

```text
Order SO1001
```

Expected checks: {"headers": {"SAPMessageDigest": "IQ0KBb7h6nsm9Qg8Q9MXmUprhN/w/3Gn/09cJRnidQhn0wXWG1KRuyq76S+O7KfPd7sTWRkSlTzzIZpHjvpcNA=="}, "exact": "Order SO1001"}

Recorded: SIMULATION / PASS

Native output:

```text
Order SO1001
```

Java type: java.lang.String; bytes: 12

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## HASH13_SHA1 — Digest SHA-1 (legacy demonstration only)

Input / fixture:

```text
Order SO1001
```

Expected checks: {"headers": {"SAPMessageDigest": "XO7LYqMDMrSlgNH5qYiC9NaHcCE="}, "exact": "Order SO1001"}

Recorded: SIMULATION / PASS

Native output:

```text
Order SO1001
```

Java type: java.lang.String; bytes: 12

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## HASH13_MD5 — Digest MD5 (legacy demonstration only)

Input / fixture:

```text
Order SO1001
```

Expected checks: {"headers": {"SAPMessageDigest": "luDV5IRQeZkG2PdCtrWB+Q=="}, "exact": "Order SO1001"}

Recorded: SIMULATION / PASS

Native output:

```text
Order SO1001
```

Java type: java.lang.String; bytes: 12

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## HASH13_EDGE0 — SHA-256 exact UTF-8 bytes

Input / fixture:

```text
[empty body]
```

Expected checks: {"headers": {"SAPMessageDigest": "47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU="}, "exact": ""}

Recorded: SIMULATION / PASS

Native output:

```text
[empty body]
```

Java type: java.lang.String; bytes: 0

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## HASH13_EDGE1 — SHA-256 exact UTF-8 bytes

Input / fixture:

```text
José సాయి
```

Expected checks: {"headers": {"SAPMessageDigest": "DswVv4bLLgYM3YxwTYGuKsu3q9FMc6kNydYNVAJu2NE="}, "exact": "José సాయి"}

Recorded: SIMULATION / PASS

Native output:

```text
José సాయి
```

Java type: java.lang.String; bytes: 18

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## HASH13_EDGE2 — SHA-256 exact UTF-8 bytes

Input / fixture:

```text
line1
line2
```

Expected checks: {"headers": {"SAPMessageDigest": "aDN24pCCm0gsJlV0XK/6eh3M+hCvqmLawrQt1saND4M="}, "exact": "line1\nline2"}

Recorded: SIMULATION / PASS

Native output:

```text
line1
line2
```

Java type: java.lang.String; bytes: 11

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Message Digest

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Filter (XPath) |  | Enter an XPath expression to select from the message | When applicable |
| Processing | Canonicalization Method | none | XML Canonicalization Version 1.0, XML Canonicalization with Comments Version 1.0, Exclusive XML Canonicalization Version 1.0, Exclusive XML Canonicalization with Comments Version 1.0, XML Canonicaliza … [270 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | Digest Algorithm | SHA-256 | SHA1, SHA256, SHA384, SHA512, MD5 | When applicable |
| Processing | Target Header | SAPMessageDigest | Enter the name of the message header where the resulting value will be set | When applicable |

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/LoadLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [821 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

### Router

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Throw Exception | false | Exception is thrown only if the sender channel is either IDoc or SOAP adapter, and the default route contains only Terminate End Event. | When applicable |
| Processing | conditionTable | [{"branchId": {"value": "SequenceFlow_200", "defaultValue": "", "key": ""}, "defaultBranch": {"value": "false", "defaultValue": "", "key": ""}, "conditionValue" … [2066 characters; full value in scenario/evidence JSON] |  | When applicable |
| Processing | Order |  |  | When applicable |
| Processing | Route Name |  |  | When applicable |
| Processing | Condition Expression |  |  | When applicable |
| Processing | Default Route |  |  | When applicable |

Saved design messages: [{"severity": "Warning", "message": "Script may not pass XML message to Message Digest. Message Digest supports XML input only.", "elementId": "CallActivity_220", "elementType": "CallActivity", "location": "MD5 - Message Digest", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 013 - SHA256 Digest.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}, {"severity": "Warning", "message": "Script may not pass XML message to Message Digest. Message Digest supports XML input only.", "elementId": "CallActivity_160", "elementType": "CallActivity", "location": "SHA-512 - Message Digest", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 013 - SHA256 Digest.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}, {"severity": "Warning", "message": "Script may not pass XML message to Message Digest. Message Digest supports XML input only.", "elementId": "CallActivity_100", "elementType": "CallActivit … [2029 characters; full value in scenario/evidence JSON]

# TRN 014 - General Splitter Gather

Technical ID: IF_TRN_014_General_Splitter_Gather. Version 3.0.0.

Default Configure → ScenarioId=SP14_TWO. Save and deploy once.

Configuration branches and overrides: {"base": {}, "stream": {"overrides": {"0": {"Streaming": "true"}}}, "parallel": {"overrides": {"0": {"ParallelProcessing": "true"}}}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| SP14_TWO | Two order records | base | SIMULATION: PASS |
| SP14_ONE | Single order record | base | SIMULATION: PASS |
| SP14_STREAM | Streaming larger order batch | stream | SIMULATION: PASS |
| SP14_PARALLEL | Parallel split; order-independent verification | parallel | SIMULATION: PASS |
| SP14_EMPTY | Zero matching records: measure splitter behaviour | base | SIMULATION: OBSERVED |
| SP14_BAD | Malformed XML rejected | base | SIMULATION: EXPECTED_REJECTION |

## SP14_TWO — Two order records

Input / fixture:

```text
<orders><order><id>1</id></order><order><id>2</id></order></orders>
```

Expected checks: {"xmlValues": {"id": ["1", "2"]}}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><multimap:Messages xmlns:multimap="http://sap.com/xi/XI/SplitAndMerge"><multimap:Message1><orders><order><id>1</id></order></orders><orders><order><id>2</id></order></orders></multimap:Message1></multimap:Messages>
```

Java type: [B; bytes: 252

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SP14_ONE — Single order record

Input / fixture:

```text
<orders><order><id>1</id></order></orders>
```

Expected checks: {"xmlValues": {"id": ["1"]}}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><multimap:Messages xmlns:multimap="http://sap.com/xi/XI/SplitAndMerge"><multimap:Message1><orders><order><id>1</id></order></orders></multimap:Message1></multimap:Messages>
```

Java type: [B; bytes: 210

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SP14_STREAM — Streaming larger order batch

Input / fixture:

```text
<orders><order><id>1</id></order><order><id>2</id></order><order><id>3</id></order><order><id>4</id></order><order><id>5</id></order><order><id>6</id></order><order><id>7</id></order><order><id>8</id></order><order><id>9</id></order><order><id>10</id></order><order><id>11</id></order><order><id>12</id></order><order><id>13</id></order><order><id>14</id></order><order><id>15</id></order><order><id>16</id></order><order><id>17</id></order><order><id>18</id></order><order><id>19</id></order><order><id>20</id></order></orders>
```

Expected checks: {"xmlValues": {"id": ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"]}}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><multimap:Messages xmlns:multimap="http://sap.com/xi/XI/SplitAndMerge"><multimap:Message1><orders><order><id>1</id></order></orders><orders><order><id>2</id></order></orders><orders><order><id>3</id></order></orders><orders><order><id>4</id></order></orders><orders><order><id>5</id></order></orders><orders><order><id>6</id></order></orders><orders><order><id>7</id></order></orders><orders><order><id>8</id></order></orders><orders><order><id>9</id></order></orders><orders><order><id>10</id></order></orders><orders><order><id>11</id></order></orders><orders><order><id>12</id></order></orders><orders><order><id>13</id></order></orders><orders><order><id>14</id></order></orders><orders><order><id>15</id></order></orders><orders><order><id>16</id></order></orders><orders><order><id>17</id></order></orders><orders><order><id>18</id></order></orders><orders><order><id>19</id></order></orders><orders><order><id>20</id></order></orders></multimap:Message1>< … [1019 characters; full value in scenario/evidence JSON]
```

Java type: [B; bytes: 1019

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SP14_PARALLEL — Parallel split; order-independent verification

Input / fixture:

```text
<orders><order><id>1</id></order><order><id>2</id></order></orders>
```

Expected checks: {"xmlValuesUnordered": {"id": ["1", "2"]}}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><multimap:Messages xmlns:multimap="http://sap.com/xi/XI/SplitAndMerge"><multimap:Message1><orders><order><id>1</id></order></orders><orders><order><id>2</id></order></orders></multimap:Message1></multimap:Messages>
```

Java type: [B; bytes: 252

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SP14_EMPTY — Zero matching records: measure splitter behaviour

Input / fixture:

```text
<orders/>
```

Expected checks: {"observe": true}

Recorded: SIMULATION / OBSERVED

Native output:

```text
<orders/>
```

Java type: java.lang.String; bytes: 9

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SP14_BAD — Malformed XML rejected

Input / fixture:

```text
<orders><order>
```

Expected checks: {"negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
com.ctc.wstx.exc.WstxEOFException: Unexpected EOF; was expecting a close tag for element <order>
 at [row,col {unknown-source}]: [1,15] on the exchange: Exchange[8D86F41530D1342-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### General Splitter

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Expression Type | XPath | XPath, Line Break | When applicable |
| Processing | XPath Expression | /orders/order | Enter an Xpath expression to navigate to the split item using absolute (e.g. /orderitems/order) or relative (//order) path. <br>Example for path with namespace: /orderitems/n0:order xmlns:n0="http://abc. … [208 characters; full value in scenario/evidence JSON] | Conditional |
| Processing | Grouping |  | Specify the group size into which the composite message should be split.<br>E.g. If a message has 4 nodes the grouping specified is 2, the message gets split into 2 individual messages with 2 nodes each. | When applicable |
| Processing | Streaming | false | Select this option to stream the process of splitting a big composite message into series of messages in small chunks. | When applicable |
| Processing | Parallel Processing | false | Select this option to enable concurrent processing of the split messages. Also, specify the timeout value. | When applicable |
| Processing | Number of Concurrent Processes | 10 | Enter the number of processes running in parallel. Maximum value allowed is 50. | Conditional |
| Processing | Timeout(in s) | 300 | If the concurrent processing of the split messages does not respond in the specified time, the splitter operation is aborted and the next elements in the sequence flow are processed. | Conditional |
| Processing | Stop on Exception | true | You have the option to stop message processing immediately if an exception occurs.<br>If you disable this option, message splitting and processing of the submessages continues, even if one of them fails. | When applicable |

### Gather

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Aggregation Strategy | Incoming Format | SameXMLFormat | Any, XML (Same Format), XML (Different Format), Plain Text | When applicable |
| Aggregation Strategy | Aggregation Algorithm | sap-identical-multi-mapping | Combine, Combine at XPath, Concatenate, Zip, Tar | When applicable |
| Aggregation Strategy | Combine from source (XPath) |  |  | Conditional |
| Aggregation Strategy | Combine at target (XPath) |  |  | Conditional |
| Aggregation Strategy | File Name |  | Configure a simple expression to define unique file names within the archive. If not specified, the header CamelFileName is evaluated. In case neither an expression is defined nor CamelFileName is ava … [255 characters; full value in scenario/evidence JSON] | Conditional |

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/LoadLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [811 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

### Router

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Throw Exception | false | Exception is thrown only if the sender channel is either IDoc or SOAP adapter, and the default route contains only Terminate End Event. | When applicable |
| Processing | conditionTable | [{"branchId": {"value": "SequenceFlow_200", "defaultValue": "", "key": ""}, "defaultBranch": {"value": "false", "defaultValue": "", "key": ""}, "conditionValue" … [1199 characters; full value in scenario/evidence JSON] |  | When applicable |
| Processing | Order |  |  | When applicable |
| Processing | Route Name |  |  | When applicable |
| Processing | Condition Expression |  |  | When applicable |
| Processing | Default Route |  |  | When applicable |

Saved design messages: []

# TRN 015 - Iterating Splitter Gather

Technical ID: IF_TRN_015_Iterating_Splitter_Gather. Version 3.0.0.

Default Configure → ScenarioId=SP15_TWO. Save and deploy once.

Configuration branches and overrides: {"base": {}, "stream": {"overrides": {"0": {"Streaming": "true"}}}, "parallel": {"overrides": {"0": {"ParallelProcessing": "true"}}}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| SP15_TWO | Two order records | base | SIMULATION: PASS |
| SP15_ONE | Single order record | base | SIMULATION: PASS |
| SP15_STREAM | Streaming larger order batch | stream | SIMULATION: PASS |
| SP15_PARALLEL | Parallel split; order-independent verification | parallel | SIMULATION: PASS |
| SP15_EMPTY | Zero matching records: measure splitter behaviour | base | SIMULATION: OBSERVED |
| SP15_BAD | Malformed XML rejected | base | SIMULATION: EXPECTED_REJECTION |

## SP15_TWO — Two order records

Input / fixture:

```text
<orders><order><id>1</id></order><order><id>2</id></order></orders>
```

Expected checks: {"xmlValues": {"id": ["1", "2"]}}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><multimap:Messages xmlns:multimap="http://sap.com/xi/XI/SplitAndMerge"><multimap:Message1><order><id>1</id></order><order><id>2</id></order></multimap:Message1></multimap:Messages>
```

Java type: [B; bytes: 218

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SP15_ONE — Single order record

Input / fixture:

```text
<orders><order><id>1</id></order></orders>
```

Expected checks: {"xmlValues": {"id": ["1"]}}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><multimap:Messages xmlns:multimap="http://sap.com/xi/XI/SplitAndMerge"><multimap:Message1><order><id>1</id></order></multimap:Message1></multimap:Messages>
```

Java type: [B; bytes: 193

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SP15_STREAM — Streaming larger order batch

Input / fixture:

```text
<orders><order><id>1</id></order><order><id>2</id></order><order><id>3</id></order><order><id>4</id></order><order><id>5</id></order><order><id>6</id></order><order><id>7</id></order><order><id>8</id></order><order><id>9</id></order><order><id>10</id></order><order><id>11</id></order><order><id>12</id></order><order><id>13</id></order><order><id>14</id></order><order><id>15</id></order><order><id>16</id></order><order><id>17</id></order><order><id>18</id></order><order><id>19</id></order><order><id>20</id></order></orders>
```

Expected checks: {"xmlValues": {"id": ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"]}}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><multimap:Messages xmlns:multimap="http://sap.com/xi/XI/SplitAndMerge"><multimap:Message1><order><id>1</id></order><order><id>2</id></order><order><id>3</id></order><order><id>4</id></order><order><id>5</id></order><order><id>6</id></order><order><id>7</id></order><order><id>8</id></order><order><id>9</id></order><order><id>10</id></order><order><id>11</id></order><order><id>12</id></order><order><id>13</id></order><order><id>14</id></order><order><id>15</id></order><order><id>16</id></order><order><id>17</id></order><order><id>18</id></order><order><id>19</id></order><order><id>20</id></order></multimap:Message1></multimap:Messages>
```

Java type: [B; bytes: 679

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SP15_PARALLEL — Parallel split; order-independent verification

Input / fixture:

```text
<orders><order><id>1</id></order><order><id>2</id></order></orders>
```

Expected checks: {"xmlValuesUnordered": {"id": ["1", "2"]}}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><multimap:Messages xmlns:multimap="http://sap.com/xi/XI/SplitAndMerge"><multimap:Message1><order><id>1</id></order><order><id>2</id></order></multimap:Message1></multimap:Messages>
```

Java type: [B; bytes: 218

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SP15_EMPTY — Zero matching records: measure splitter behaviour

Input / fixture:

```text
<orders/>
```

Expected checks: {"observe": true}

Recorded: SIMULATION / OBSERVED

Native output:

```text
<orders/>
```

Java type: java.lang.String; bytes: 9

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SP15_BAD — Malformed XML rejected

Input / fixture:

```text
<orders><order>
```

Expected checks: {"negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
com.ctc.wstx.exc.WstxEOFException: Unexpected EOF; was expecting a close tag for element <order>
 at [row,col {unknown-source}]: [1,15] on the exchange: Exchange[94EEFD435329A5B-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Iterating Splitter

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Expression Type | XPath | XPath, Token, Line Break | When applicable |
| Processing | XPath Expression | /orders/order | Enter an Xpath expression to navigate to the split item using absolute (e.g. /orderitems/order) or relative (//order) path. <br>Example for path with namespace: /orderitems/n0:order xmlns:n0="http://abc. … [209 characters; full value in scenario/evidence JSON] | Conditional |
| Processing | Token | /orders/order | Enter a Token to split the message. Token can be a node name in the XML message.<br>E.g. Enter order to split a message at every order node. | Conditional |
| Processing | Grouping |  | Specify the group size into which the composite message should be split.<br>E.g. If a message has 4 nodes the grouping specified is 2, the message gets split into 2 individual messages with 2 nodes each. | When applicable |
| Processing | Streaming | false | Select this option to stream the process of splitting a big composite message into series of messages in small chunks. | When applicable |
| Processing | Parallel Processing | false | Select this option to enable concurrent processing of the split messages. Also, specify the timeout value. | When applicable |
| Processing | Number of Concurrent Processes | 10 | Enter the number of processes running in parallel. Maximum value allowed is 50. | Conditional |
| Processing | Timeout(in s) | 300 | If the concurrent processing of the split messages does not respond in the specified time, the splitter operation is aborted and the next elements in the sequence flow are processed. | Conditional |
| Processing | Stop on Exception | true | You have the option to stop message processing immediately if an exception occurs.<br>If you disable this option, message splitting and processing of the submessages continues, even if one of them fails. | When applicable |

### Gather

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Aggregation Strategy | Incoming Format | SameXMLFormat | Any, XML (Same Format), XML (Different Format), Plain Text | When applicable |
| Aggregation Strategy | Aggregation Algorithm | sap-identical-multi-mapping | Combine, Combine at XPath, Concatenate, Zip, Tar | When applicable |
| Aggregation Strategy | Combine from source (XPath) |  |  | Conditional |
| Aggregation Strategy | Combine at target (XPath) |  |  | Conditional |
| Aggregation Strategy | File Name |  | Configure a simple expression to define unique file names within the archive. If not specified, the header CamelFileName is evaluated. In case neither an expression is defined nor CamelFileName is ava … [255 characters; full value in scenario/evidence JSON] | Conditional |

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/LoadLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [811 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

### Router

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Throw Exception | false | Exception is thrown only if the sender channel is either IDoc or SOAP adapter, and the default route contains only Terminate End Event. | When applicable |
| Processing | conditionTable | [{"branchId": {"value": "SequenceFlow_200", "defaultValue": "", "key": ""}, "defaultBranch": {"value": "false", "defaultValue": "", "key": ""}, "conditionValue" … [1199 characters; full value in scenario/evidence JSON] |  | When applicable |
| Processing | Order |  |  | When applicable |
| Processing | Route Name |  |  | When applicable |
| Processing | Condition Expression |  |  | When applicable |
| Processing | Default Route |  |  | When applicable |

Saved design messages: []

# TRN 016 - Line Splitter Gather

Technical ID: IF_TRN_016_Line_Splitter_Gather. Version 3.0.0.

Default Configure → ScenarioId=LINE16_LF. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| LINE16_LF | LF records | base | SIMULATION: PASS |
| LINE16_CRLF | CRLF records | base | SIMULATION: PASS |
| LINE16_ONE | One record | base | SIMULATION: PASS |
| LINE16_UNICODE | Unicode lines | base | SIMULATION: PASS |
| LINE16_EMPTY | Empty line handling | base | SIMULATION: OBSERVED |

## LINE16_LF — LF records

Input / fixture:

```text
Anu
Ravi
```

Expected checks: {"exact": "AnuRavi"}

Recorded: SIMULATION / PASS

Native output:

```text
AnuRavi
```

Java type: [B; bytes: 7

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## LINE16_CRLF — CRLF records

Input / fixture:

```text
Anu\u000d
Ravi
```

Expected checks: {"exact": "AnuRavi"}

Recorded: SIMULATION / PASS

Native output:

```text
AnuRavi
```

Java type: [B; bytes: 7

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## LINE16_ONE — One record

Input / fixture:

```text
Anu
```

Expected checks: {"exact": "Anu"}

Recorded: SIMULATION / PASS

Native output:

```text
Anu
```

Java type: [B; bytes: 3

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## LINE16_UNICODE — Unicode lines

Input / fixture:

```text
José
సాయి
```

Expected checks: {"exact": "Joséసాయి"}

Recorded: SIMULATION / PASS

Native output:

```text
Joséసాయి
```

Java type: [B; bytes: 17

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## LINE16_EMPTY — Empty line handling

Input / fixture:

```text
Anu

Ravi
```

Expected checks: {"observe": true}

Recorded: SIMULATION / OBSERVED

Native output:

```text
AnuRavi
```

Java type: [B; bytes: 7

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Gather

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Aggregation Strategy | Incoming Format | PlainText | Any, XML (Same Format), XML (Different Format), Plain Text | When applicable |
| Aggregation Strategy | Aggregation Algorithm | concatenate-strategy | Combine, Combine at XPath, Concatenate, Zip, Tar | When applicable |
| Aggregation Strategy | Combine from source (XPath) |  |  | Conditional |
| Aggregation Strategy | Combine at target (XPath) |  |  | Conditional |
| Aggregation Strategy | File Name |  | Configure a simple expression to define unique file names within the archive. If not specified, the header CamelFileName is evaluated. In case neither an expression is defined nor CamelFileName is ava … [255 characters; full value in scenario/evidence JSON] | Conditional |

### Iterating Splitter

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Expression Type | Line Break | XPath, Token, Line Break | When applicable |
| Processing | XPath Expression |  | Enter an Xpath expression to navigate to the split item using absolute (e.g. /orderitems/order) or relative (//order) path. <br>Example for path with namespace: /orderitems/n0:order xmlns:n0="http://abc. … [209 characters; full value in scenario/evidence JSON] | Conditional |
| Processing | Token |  | Enter a Token to split the message. Token can be a node name in the XML message.<br>E.g. Enter order to split a message at every order node. | Conditional |
| Processing | Grouping |  | Specify the group size into which the composite message should be split.<br>E.g. If a message has 4 nodes the grouping specified is 2, the message gets split into 2 individual messages with 2 nodes each. | When applicable |
| Processing | Streaming | false | Select this option to stream the process of splitting a big composite message into series of messages in small chunks. | When applicable |
| Processing | Parallel Processing | false | Select this option to enable concurrent processing of the split messages. Also, specify the timeout value. | When applicable |
| Processing | Number of Concurrent Processes | 10 | Enter the number of processes running in parallel. Maximum value allowed is 50. | Conditional |
| Processing | Timeout(in s) | 300 | If the concurrent processing of the split messages does not respond in the specified time, the splitter operation is aborted and the next elements in the sequence flow are processed. | Conditional |
| Processing | Stop on Exception | true | You have the option to stop message processing immediately if an exception occurs.<br>If you disable this option, message splitting and processing of the submessages continues, even if one of them fails. | When applicable |

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [813 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: []

# TRN 017 - Data Store Roundtrip

Technical ID: IF_TRN_017_Data_Store_Roundtrip. Version 3.0.0.

Default Configure → ScenarioId=DS17_XML. Save and deploy once.

Configuration branches and overrides: {"base": {}, "deleteget": {"overrides": {"1": {"delete": "true"}}}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| DS17_XML | Write, get and delete own entry | base | LIVE: PASS |
| DS17_UNICODE | UTF-8 stored value | base | LIVE: PASS |
| DS17_JSON | Store and retrieve JSON bytes | base | LIVE: PASS |
| DS17_DELETEGET | Get with delete-on-read | deleteget | LIVE: PASS |

## DS17_XML — Write, get and delete own entry

Input / fixture:

```text
<student><id>1</id><name>Anu</name></student>
```

Expected checks: {"exact": "<student><id>1</id><name>Anu</name></student>"}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_Expected": "COMPLETED", "TRN_OutputPreview": "<student><id>1</id><name>Anu</name></student>", "TRN_TestResult": "PASS", "TRN_OutputSha256": "MIBhexjFOtM21nFXIFxm/fYgqhTreU2nDHW9YF2V2ec=", "TRN_ScenarioId": "DS17_XML"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## DS17_UNICODE — UTF-8 stored value

Input / fixture:

```text
José సాయి
```

Expected checks: {"exact": "José సాయి"}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_OutputSha256": "DswVv4bLLgYM3YxwTYGuKsu3q9FMc6kNydYNVAJu2NE=", "TRN_ScenarioId": "DS17_UNICODE", "TRN_Expected": "COMPLETED", "TRN_OutputPreview": "José సాయి", "TRN_TestResult": "PASS"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## DS17_JSON — Store and retrieve JSON bytes

Input / fixture:

```text
{"orderId":"SO1001","amount":250}
```

Expected checks: {"exact": "{\"orderId\":\"SO1001\",\"amount\":250}"}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_ScenarioId": "DS17_JSON", "TRN_OutputSha256": "S0uRozu7bWt9s7L70qTL1TykoDkbpmqcZrpetuFglt4=", "TRN_Expected": "COMPLETED", "TRN_TestResult": "PASS", "TRN_OutputPreview": "{\"orderId\":\"SO1001\",\"amount\":250}"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## DS17_DELETEGET — Get with delete-on-read

Input / fixture:

```text
<student>Anu</student>
```

Expected checks: {"exact": "<student>Anu</student>"}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_OutputSha256": "KlRnzcZZGyE8ve3Rq7btOPVmgWYMUP2ODNylQEyROsw=", "TRN_OutputPreview": "<student>Anu</student>", "TRN_TestResult": "PASS", "TRN_Expected": "COMPLETED", "TRN_ScenarioId": "DS17_DELETEGET"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### Router

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Throw Exception | false | Exception is thrown only if the sender channel is either IDoc or SOAP adapter, and the default route contains only Terminate End Event. | When applicable |
| Processing | conditionTable | [{"branchId": {"value": "SequenceFlow_200", "defaultValue": "", "key": ""}, "defaultBranch": {"value": "false", "defaultValue": "", "key": ""}, "conditionValue" … [768 characters; full value in scenario/evidence JSON] |  | When applicable |
| Processing | Order |  |  | When applicable |
| Processing | Route Name |  |  | When applicable |
| Processing | Condition Expression |  |  | When applicable |
| Processing | Default Route |  |  | When applicable |

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Get

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Data Store Name | TRN017_STUDENTS | Enter a data store name (maximum 40 characters). The name should begin with an alphabetic character and use characters (aA-zZ, 0-9, - _ . ~ ) without any whitespace. You can also enter ${header.header … [298 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | Visibility | local | Integration Flow, Global | When applicable |
| Processing | Entry ID | ${property.EntryId} | Specify the corresponding Entry ID of the content to be fetched from data store. You can enter ${header.headername}, ${property.propertyname} or ${xpath.node} to read the Entry ID from the incoming me … [287 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | Delete On Completion | false | Select this option to delete messages from data store after successfully processing the messages | When applicable |
| Processing | Throw Exception on Missing Entry | true | You have the option to throw an exception in the message processing if the entry with the specified ID does not exist in the datastore. If you disable this option, the header SAP_DataStoreEntryFound i … [277 characters; full value in scenario/evidence JSON] | When applicable |

### Delete

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Data Store Name | TRN017_STUDENTS | Enter a data store name (maximum 40 characters). The name should begin with an alphabetic character and use characters (aA-zZ, 0-9, - _ . ~ ) without any whitespace. You can also enter ${header.header … [298 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | Visibility | local | Integration Flow, Global | When applicable |
| Processing | Entry ID | ${property.EntryId} | Specify the corresponding Entry ID of the content to be deleted from data store. You can enter a dynamic expression for a header, a property or an XPath to read the Entry ID from the incoming message. … [454 characters; full value in scenario/evidence JSON] | When applicable |

### Write

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Data Store Name | TRN017_STUDENTS | Enter a data store name (maximum 40 characters). The name should begin with an alphabetic character and use characters (aA-zZ, 0-9, - _ . ~ ) without any whitespace. You can also enter ${header.header … [298 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | Visibility | local | Integration Flow, Global | When applicable |
| Processing | Entry ID | ${property.EntryId} | Entry id with which content will be persisted, you can use ${header.headername}, ${property.propertyname} or ${xpath.node} to read entry id details from incoming message. A generated GUID would be use … [223 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | Retention Threshold for Alerting (in d) | 1 | Enter a time period (in days). If messages are not fetched within this period, an alert is raised. The default value is 2. | When applicable |
| Processing | Expiration Period (in d) | 2 | Enter a time period (in days). The stored message is automatically deleted from the data store after this period. The default value is 30 days. | When applicable |
| Processing | Encrypt Stored Message | true | Select this option to encrypt the message in data store | When applicable |
| Processing | Overwrite Existing Message | true | Select this option to overwrite the message in data store | When applicable |
| Processing | Include Message Headers | false | Select this option to also store the message's headers | When applicable |

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [831 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: []

# TRN 018 - Data Store Missing Entry

Technical ID: IF_TRN_018_Data_Store_Missing_Entry. Version 3.0.0.

Default Configure → ScenarioId=DS18_REQUIRED. Save and deploy once.

Configuration branches and overrides: {"base": {}, "optional": {"overrides": {"0": {"stopOnMissingEntry": "false"}}}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| DS18_REQUIRED | Missing required entry fails | base | LIVE: PASS |
| DS18_OTHER | Another missing entry also fails | base | LIVE: PASS |
| DS18_OPTIONAL | Optional missing entry behaviour | optional | LIVE: PASS |

## DS18_REQUIRED — Missing required entry fails

Input / fixture:

```text
test
```

Expected checks: {"negative": true}

Recorded: LIVE / PASS; runtime FAILED

```text
b'com.sap.esb.datastore.MessageNotFoundException: No message found for id TRN_LAB_3907e3f2-6013-45a0-b9b6-c8a06259dd40 in store TRN018_MISSING for integration flow IF_TRN_018_Data_Store_Missing_Entry'
```

Live checks: {"TRN_ScenarioId": "DS18_REQUIRED", "TRN_Expected": "FAILED"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## DS18_OTHER — Another missing entry also fails

Input / fixture:

```text
other input
```

Expected checks: {"negative": true}

Recorded: LIVE / PASS; runtime FAILED

```text
b'com.sap.esb.datastore.MessageNotFoundException: No message found for id TRN_LAB_d3dee4a4-e3eb-4778-b3b5-e9ddca30db25 in store TRN018_MISSING for integration flow IF_TRN_018_Data_Store_Missing_Entry'
```

Live checks: {"TRN_Expected": "FAILED", "TRN_ScenarioId": "DS18_OTHER"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## DS18_OPTIONAL — Optional missing entry behaviour

Input / fixture:

```text
keep this body
```

Expected checks: {"observe": true}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_TestResult": "OBSERVED", "TRN_ScenarioId": "DS18_OPTIONAL", "TRN_Expected": "COMPLETED", "TRN_OutputPreview": "keep this body", "TRN_OutputSha256": "oZvOs1WwVByQ4DJ8BmOg8n542lUZlCQMYoZgjsEoPB8="}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### Router

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Throw Exception | false | Exception is thrown only if the sender channel is either IDoc or SOAP adapter, and the default route contains only Terminate End Event. | When applicable |
| Processing | conditionTable | [{"branchId": {"value": "SequenceFlow_200", "defaultValue": "", "key": ""}, "defaultBranch": {"value": "false", "defaultValue": "", "key": ""}, "conditionValue" … [767 characters; full value in scenario/evidence JSON] |  | When applicable |
| Processing | Order |  |  | When applicable |
| Processing | Route Name |  |  | When applicable |
| Processing | Condition Expression |  |  | When applicable |
| Processing | Default Route |  |  | When applicable |

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Get

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Data Store Name | TRN018_MISSING | Enter a data store name (maximum 40 characters). The name should begin with an alphabetic character and use characters (aA-zZ, 0-9, - _ . ~ ) without any whitespace. You can also enter ${header.header … [298 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | Visibility | local | Integration Flow, Global | When applicable |
| Processing | Entry ID | ${property.EntryId} | Specify the corresponding Entry ID of the content to be fetched from data store. You can enter ${header.headername}, ${property.propertyname} or ${xpath.node} to read the Entry ID from the incoming me … [287 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | Delete On Completion | false | Select this option to delete messages from data store after successfully processing the messages | When applicable |
| Processing | Throw Exception on Missing Entry | false | You have the option to throw an exception in the message processing if the entry with the specified ID does not exist in the datastore. If you disable this option, the header SAP_DataStoreEntryFound i … [277 characters; full value in scenario/evidence JSON] | When applicable |

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [841 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: []

# TRN 019 - Persist Message

Technical ID: IF_TRN_019_Persist_Message. Version 3.0.0.

Default Configure → ScenarioId=PERSIST19_XML. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| PERSIST19_XML | Persist an XML snapshot | base | LIVE: PASS |
| PERSIST19_JSON | Persist JSON snapshot | base | LIVE: PASS |
| PERSIST19_UNICODE | Persist Unicode snapshot | base | LIVE: PASS |
| PERSIST19_MULTI | Persist line endings | base | LIVE: PASS |

## PERSIST19_XML — Persist an XML snapshot

Input / fixture:

```text
<order><id>SO1001</id></order>
```

Expected checks: {"exact": "<order><id>SO1001</id></order>"}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_OutputPreview": "<order><id>SO1001</id></order>", "TRN_OutputSha256": "5wIca79JhpWmIQdbIB7d5c84OHzhJlyeMQfvRb1i8EQ=", "TRN_Expected": "COMPLETED", "TRN_ScenarioId": "PERSIST19_XML", "TRN_TestResult": "PASS"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## PERSIST19_JSON — Persist JSON snapshot

Input / fixture:

```text
{"orderId":"SO1002"}
```

Expected checks: {"exact": "{\"orderId\":\"SO1002\"}"}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_OutputSha256": "bfnRyXiwbu2/YOLar7R9Bchp9z0bElhckgUsVCR5U1Y=", "TRN_TestResult": "PASS", "TRN_ScenarioId": "PERSIST19_JSON", "TRN_OutputPreview": "{\"orderId\":\"SO1002\"}", "TRN_Expected": "COMPLETED"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## PERSIST19_UNICODE — Persist Unicode snapshot

Input / fixture:

```text
José సాయి
```

Expected checks: {"exact": "José సాయి"}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_OutputSha256": "DswVv4bLLgYM3YxwTYGuKsu3q9FMc6kNydYNVAJu2NE=", "TRN_OutputPreview": "José సాయి", "TRN_TestResult": "PASS", "TRN_ScenarioId": "PERSIST19_UNICODE", "TRN_Expected": "COMPLETED"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## PERSIST19_MULTI — Persist line endings

Input / fixture:

```text
line1
line2
```

Expected checks: {"exact": "line1\nline2"}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_OutputPreview": "line1 line2", "TRN_Expected": "COMPLETED", "TRN_TestResult": "PASS", "TRN_ScenarioId": "PERSIST19_MULTI", "TRN_OutputSha256": "aDN24pCCm0gsJlV0XK/6eh3M+hCvqmLawrQt1saND4M="}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### Persist Message

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Step ID | TRN019PERSISTSTUDENT | Enter a unique ID for the process step at which message payload is stored | When applicable |
| Processing | Encrypt Stored Message | true | Encrypt the stored message payload | When applicable |

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/LoadLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [841 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: []

# TRN 020 - MIME Roundtrip

Technical ID: IF_TRN_020_MIME_Roundtrip. Version 3.0.0.

Default Configure → ScenarioId=L020_1. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| L020_1 | MIME encode/decode round trip — business text | base | SIMULATION: PASS |
| L020_2 | MIME encode/decode round trip — UTF-8 Unicode | base | SIMULATION: PASS |
| L020_3 | MIME encode/decode round trip — line endings | base | SIMULATION: PASS |
| L020_4 | MIME encode/decode round trip — empty body | base | SIMULATION: PASS |
| L020_5 | MIME encode/decode round trip — repetitive larger payload | base | SIMULATION: PASS |

## L020_1 — MIME encode/decode round trip — business text

Input / fixture:

```text
Order SO1001, amount 250
```

Expected checks: {"exact": "Order SO1001, amount 250"}

Recorded: SIMULATION / PASS

Native output:

```text
Order SO1001, amount 250
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 24

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## L020_2 — MIME encode/decode round trip — UTF-8 Unicode

Input / fixture:

```text
José సాయి 😀
```

Expected checks: {"exact": "José సాయి 😀"}

Recorded: SIMULATION / PASS

Native output:

```text
José సాయి 😀
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 23

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## L020_3 — MIME encode/decode round trip — line endings

Input / fixture:

```text
line1
line2\u000d
line3
```

Expected checks: {"exact": "line1\nline2\r\nline3"}

Recorded: SIMULATION / PASS

Native output:

```text
line1
line2\u000d
line3
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 18

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## L020_4 — MIME encode/decode round trip — empty body

Input / fixture:

```text
[empty body]
```

Expected checks: {"exact": ""}

Recorded: SIMULATION / PASS

Native output:

```text
[empty body]
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 0

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## L020_5 — MIME encode/decode round trip — repetitive larger payload

Input / fixture:

```text
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx … [4096 characters; full value in scenario/evidence JSON]
```

Expected checks: {"exact": "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx … [4109 characters; full value in scenario/evidence JSON]

Recorded: SIMULATION / PASS

Native output:

```text
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx … [4096 characters; full value in scenario/evidence JSON]
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 4096

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### MIME Multipart Decoder

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Multipart Headers Inline | true | Select if the MIME headers of the multipart are part of the message body instead of a message header | When applicable |

### MIME Multipart Encoder

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Multipart Subtype | Mixed | Alternative, Digest, Mixed, Parallel, Related | When applicable |
| Processing | Add Multipart Headers Inline | true | Select to add the MIME headers of the multipart as part of the message body instead of a message header | When applicable |
| Processing | Include Headers |  | Specify a regular expression matching the message headers to be included as MIME headers in the MIME multipart. Example: headerA\|headerB\|MyCompany.*\|x-.* | Conditional |
| Processing | Fold Long Headers | false | If set, headers will be 'folded' by inserted line breaks. This is the preferred way when sending the message with the Mail adapter. When sending out the message using the HTTP protocol, header folding … [223 characters; full value in scenario/evidence JSON] | Conditional |

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [807 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: []

# TRN 021 - Timer Hello

Technical ID: IF_TRN_021_Timer_Hello. Version 3.0.1.

Default Configure → ScenarioId=TIMER21_START. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| TIMER21_START | Once on deployment | base | LIVE: PASS |
| TIMER21_REDEPLOY | Redeploy to produce a second one-time run | base | LIVE: PASS |
| TIMER21_UTC | Use an explicit UTC event timestamp | base | LIVE: PASS |

## TIMER21_START — Once on deployment

Input / fixture:

```text
[empty body]
```

Properties: {"StudentName": "Anu", "EventTime": "2026-11-17T09:00:00Z"}

Expected checks: {"exact": "Timer event for Anu at 2026-11-17T09:00:00Z"}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_OutputPreview": "Timer event for Anu at 2026-11-17T09:00:00Z", "TRN_TestResult": "PASS", "TRN_Expected": "COMPLETED", "TRN_ScenarioId": "TIMER21_START", "TRN_OutputSha256": "JeEHvrklO/1//CblqXN7Wa8C+3SSMGg1n5M2y9nxqZE="}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## TIMER21_REDEPLOY — Redeploy to produce a second one-time run

Input / fixture:

```text
[empty body]
```

Properties: {"StudentName": "Ravi", "EventTime": "2026-11-17T09:01:00Z"}

Expected checks: {"exact": "Timer event for Ravi at 2026-11-17T09:01:00Z"}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_Expected": "COMPLETED", "TRN_TestResult": "PASS", "TRN_ScenarioId": "TIMER21_REDEPLOY", "TRN_OutputPreview": "Timer event for Ravi at 2026-11-17T09:01:00Z", "TRN_OutputSha256": "/NeRZ8q4ZJs4vdNdwSOhY6eH+SPKep3Uus1kKgDYRWw="}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## TIMER21_UTC — Use an explicit UTC event timestamp

Input / fixture:

```text
[empty body]
```

Properties: {"StudentName": "సాయి", "EventTime": "2026-11-17T00:00:00Z"}

Expected checks: {"exact": "Timer event for సాయి at 2026-11-17T00:00:00Z"}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_ScenarioId": "TIMER21_UTC", "TRN_OutputPreview": "Timer event for సాయి at 2026-11-17T00:00:00Z", "TRN_Expected": "COMPLETED", "TRN_TestResult": "PASS", "TRN_OutputSha256": "cbmhJ62XC43bmV2qP3CHcqWvsP3k3YXnY3+5e7Cz9cA="}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | Timer event for ${property.StudentName} at ${property.EventTime} | Enter an expression or a constant to form new message body | When applicable |

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/LoadLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

Saved design messages: []

# TRN 022 - Local Process Call

Technical ID: IF_TRN_022_Local_Process_Call. Version 3.0.0.

Default Configure → ScenarioId=PC22_DEFAULT. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| PC22_DEFAULT | Local process replaces caller body | base | SIMULATION: PASS |
| PC22_UNICODE | Properties remain visible in local process | base | SIMULATION: PASS |
| PC22_EMPTY | Local process can return empty text | base | SIMULATION: PASS |
| PC22_XML | Return structured local response | base | SIMULATION: PASS |

## PC22_DEFAULT — Local process replaces caller body

Input / fixture:

```text
Caller body
```

Properties: {"LocalGreeting": "Hello from local process"}

Expected checks: {"exact": "Hello from local process"}

Recorded: SIMULATION / PASS

Native output:

```text
Hello from local process
```

Java type: java.lang.String; bytes: 24

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## PC22_UNICODE — Properties remain visible in local process

Input / fixture:

```text
Caller body
```

Properties: {"LocalGreeting": "Hello సాయి"}

Expected checks: {"exact": "Hello సాయి"}

Recorded: SIMULATION / PASS

Native output:

```text
Hello సాయి
```

Java type: java.lang.String; bytes: 18

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## PC22_EMPTY — Local process can return empty text

Input / fixture:

```text
Original body
```

Properties: {"LocalGreeting": ""}

Expected checks: {"exact": ""}

Recorded: SIMULATION / PASS

Native output:

```text
[empty body]
```

Java type: java.lang.String; bytes: 0

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## PC22_XML — Return structured local response

Input / fixture:

```text
Original body
```

Properties: {"LocalGreeting": "<result>Accepted</result>"}

Expected checks: {"xmlRoot": "result", "contains": ["Accepted"]}

Recorded: SIMULATION / PASS

Native output:

```text
<result>Accepted</result>
```

Java type: java.lang.String; bytes: 25

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Event

No additional component processing fields; inspect General and the surrounding message flow.

### Start Event

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.LocalGreeting} | Enter an expression or a constant to form new message body | When applicable |

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Process Call

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Local Integration Process | Process_2 | Select a pre-defined Local Integration Process to be invoked | When applicable |

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

Saved design messages: []

# TRN 023 - Router

Technical ID: IF_TRN_023_Router. Version 3.0.0.

Default Configure → ScenarioId=ROUTE23_HIGH. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| ROUTE23_HIGH | Above threshold → priority | base | SIMULATION: PASS |
| ROUTE23_BOUNDARY | Boundary is standard | base | SIMULATION: PASS |
| ROUTE23_LOW | Zero amount → standard | base | SIMULATION: PASS |
| ROUTE23_MISSING | Missing optional amount → default route | base | SIMULATION: PASS |
| ROUTE23_DECIMAL | Decimal over threshold | base | SIMULATION: PASS |
| ROUTE23_BAD | Malformed XML rejected | base | SIMULATION: EXPECTED_REJECTION |

## ROUTE23_HIGH — Above threshold → priority

Input / fixture:

```text
<order><amount>1500</amount></order>
```

Expected checks: {"exact": "PRIORITY"}

Recorded: SIMULATION / PASS

Native output:

```text
PRIORITY
```

Java type: java.lang.String; bytes: 8

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## ROUTE23_BOUNDARY — Boundary is standard

Input / fixture:

```text
<order><amount>1000</amount></order>
```

Expected checks: {"exact": "STANDARD"}

Recorded: SIMULATION / PASS

Native output:

```text
STANDARD
```

Java type: java.lang.String; bytes: 8

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## ROUTE23_LOW — Zero amount → standard

Input / fixture:

```text
<order><amount>0</amount></order>
```

Expected checks: {"exact": "STANDARD"}

Recorded: SIMULATION / PASS

Native output:

```text
STANDARD
```

Java type: java.lang.String; bytes: 8

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## ROUTE23_MISSING — Missing optional amount → default route

Input / fixture:

```text
<order/>
```

Expected checks: {"exact": "STANDARD"}

Recorded: SIMULATION / PASS

Native output:

```text
STANDARD
```

Java type: java.lang.String; bytes: 8

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## ROUTE23_DECIMAL — Decimal over threshold

Input / fixture:

```text
<order><amount>1000.01</amount></order>
```

Expected checks: {"exact": "PRIORITY"}

Recorded: SIMULATION / PASS

Native output:

```text
PRIORITY
```

Java type: java.lang.String; bytes: 8

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## ROUTE23_BAD — Malformed XML rejected

Input / fixture:

```text
<order>
```

Expected checks: {"negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
javax.xml.xpath.XPathExpressionException: Failure converting a node of class javax.xml.transform.sax.SAXSource: org.xml.sax.SAXParseException; lineNumber: 1; columnNumber: 8; XML document structures must start and end within the same entity. on the exchange: Exchange[D56474139A7BC57-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### Router

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Throw Exception | false | Exception is thrown only if the sender channel is either IDoc or SOAP adapter, and the default route contains only Terminate End Event. | When applicable |
| Processing | conditionTable | [{"branchId": {"value": "SequenceFlow_102", "defaultValue": "", "key": ""}, "defaultBranch": {"value": "false", "defaultValue": "", "key": ""}, "conditionValue" … [765 characters; full value in scenario/evidence JSON] |  | When applicable |
| Processing | Order |  |  | When applicable |
| Processing | Route Name |  |  | When applicable |
| Processing | Condition Expression |  |  | When applicable |
| Processing | Default Route |  |  | When applicable |

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | constant | Expression, Constant | When applicable |
| Message Body | Body | PRIORITY | Enter an expression or a constant to form new message body | When applicable |

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

Saved design messages: [{"severity": "Warning", "message": "Script may not pass XML message to Sequence Flow. Sequence Flow supports XML input only.", "elementId": "SequenceFlow_102", "elementType": "SequenceFlow", "attributeId": "iflow.advance.sequenceflow.tab", "location": "Priority", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 023 - Router.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}]

# TRN 024 - Parallel Multicast

Technical ID: IF_TRN_024_Parallel_Multicast. Version 3.0.0.

Default Configure → ScenarioId=MC24_BASE. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| MC24_BASE | Two service responses | base | SIMULATION: PASS |
| MC24_UNICODE | Unicode branch responses | base | SIMULATION: PASS |
| MC24_EMPTY | Empty branch result | base | SIMULATION: PASS |
| MC24_INVALID | Parallel Gather observes malformed branch XML without a validation guarantee | base | SIMULATION: OBSERVED |

## MC24_BASE — Two service responses

Input / fixture:

```text
<request/>
```

Properties: {"BranchA": "Sales", "BranchB": "Finance"}

Expected checks: {"xmlValuesUnordered": {"result": ["Sales", "Finance"]}}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><multimap:Messages xmlns:multimap="http://sap.com/xi/XI/SplitAndMerge"><multimap:Message1><result>Sales</result><result>Finance</result></multimap:Message1></multimap:Messages>
```

Java type: [B; bytes: 214

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## MC24_UNICODE — Unicode branch responses

Input / fixture:

```text
<request/>
```

Properties: {"BranchA": "José", "BranchB": "సాయి"}

Expected checks: {"contains": ["José", "సాయి"]}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><multimap:Messages xmlns:multimap="http://sap.com/xi/XI/SplitAndMerge"><multimap:Message1><result>José</result><result>సాయి</result></multimap:Message1></multimap:Messages>
```

Java type: [B; bytes: 219

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## MC24_EMPTY — Empty branch result

Input / fixture:

```text
<request/>
```

Properties: {"BranchA": "", "BranchB": "Finance"}

Expected checks: {"contains": ["Finance"]}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><multimap:Messages xmlns:multimap="http://sap.com/xi/XI/SplitAndMerge"><multimap:Message1><result/><result>Finance</result></multimap:Message1></multimap:Messages>
```

Java type: [B; bytes: 201

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## MC24_INVALID — Parallel Gather observes malformed branch XML without a validation guarantee

Input / fixture:

```text
<request/>
```

Properties: {"BranchA": "A & B", "BranchB": "Finance"}

Expected checks: {"observe": true}

Recorded: SIMULATION / OBSERVED

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><multimap:Messages xmlns:multimap="http://sap.com/xi/XI/SplitAndMerge"><multimap:Message1><result>Finance</result></multimap:Message1></multimap:Messages>
```

Java type: [B; bytes: 192

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | <result>${property.BranchA}</result> | Enter an expression or a constant to form new message body | When applicable |

### Gather

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Aggregation Strategy | Incoming Format | SameXMLFormat | Any, XML (Same Format), XML (Different Format), Plain Text | When applicable |
| Aggregation Strategy | Aggregation Algorithm | sap-identical-multi-mapping | Combine, Combine at XPath, Concatenate, Zip, Tar | When applicable |
| Aggregation Strategy | Combine from source (XPath) |  |  | Conditional |
| Aggregation Strategy | Combine at target (XPath) |  |  | Conditional |
| Aggregation Strategy | File Name |  | Configure a simple expression to define unique file names within the archive. If not specified, the header CamelFileName is evaluated. In case neither an expression is defined nor CamelFileName is ava … [255 characters; full value in scenario/evidence JSON] | Conditional |

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Join

No additional component processing fields; inspect General and the surrounding message flow.

### Parallel Multicast

No additional component processing fields; inspect General and the surrounding message flow.

Saved design messages: []

# TRN 025 - Groovy Uppercase

Technical ID: IF_TRN_025_Groovy_Uppercase. Version 3.0.0.

Default Configure → ScenarioId=SCRIPT25_TEXT. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| SCRIPT25_TEXT | Uppercase body | base | SIMULATION: PASS |
| SCRIPT25_UNICODE | Unicode casing | base | SIMULATION: PASS |
| SCRIPT25_EMPTY | Empty body | base | SIMULATION: PASS |
| SCRIPT25_MULTI | Preserve line separators | base | SIMULATION: PASS |
| SCRIPT25_JSON | Whole-body casing also changes JSON keys/values | base | SIMULATION: PASS |

## SCRIPT25_TEXT — Uppercase body

Input / fixture:

```text
Hello, student!
```

Expected checks: {"exact": "HELLO, STUDENT!"}

Recorded: SIMULATION / PASS

Native output:

```text
HELLO, STUDENT!
```

Java type: java.lang.String; bytes: 15

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SCRIPT25_UNICODE — Unicode casing

Input / fixture:

```text
José సాయి ß
```

Expected checks: {"exact": "JOSÉ సాయి SS"}

Recorded: SIMULATION / PASS

Native output:

```text
JOSÉ సాయి SS
```

Java type: java.lang.String; bytes: 21

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SCRIPT25_EMPTY — Empty body

Input / fixture:

```text
[empty body]
```

Expected checks: {"exact": ""}

Recorded: SIMULATION / PASS

Native output:

```text
[empty body]
```

Java type: java.lang.String; bytes: 0

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SCRIPT25_MULTI — Preserve line separators

Input / fixture:

```text
Line one
line two
```

Expected checks: {"exact": "LINE ONE\nLINE TWO"}

Recorded: SIMULATION / PASS

Native output:

```text
LINE ONE
LINE TWO
```

Java type: java.lang.String; bytes: 17

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SCRIPT25_JSON — Whole-body casing also changes JSON keys/values

Input / fixture:

```text
{"status":"new"}
```

Expected checks: {"exact": "{\"STATUS\":\"NEW\"}"}

Recorded: SIMULATION / PASS

Native output:

```text
{"STATUS":"NEW"}
```

Java type: java.lang.String; bytes: 16

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/Uppercase.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function | processData | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [821 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: []

# TRN 026 - JavaScript Uppercase

Technical ID: IF_TRN_026_JavaScript_Uppercase. Version 3.0.0.

Default Configure → ScenarioId=SCRIPT26_TEXT. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| SCRIPT26_TEXT | Uppercase body | base | SIMULATION: PASS |
| SCRIPT26_UNICODE | Unicode casing | base | SIMULATION: PASS |
| SCRIPT26_EMPTY | Empty body | base | SIMULATION: PASS |
| SCRIPT26_MULTI | Preserve line separators | base | SIMULATION: PASS |
| SCRIPT26_JSON | Whole-body casing also changes JSON keys/values | base | SIMULATION: PASS |

## SCRIPT26_TEXT — Uppercase body

Input / fixture:

```text
Hello, student!
```

Expected checks: {"exact": "HELLO, STUDENT!"}

Recorded: SIMULATION / PASS

Native output:

```text
HELLO, STUDENT!
```

Java type: java.lang.String; bytes: 15

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SCRIPT26_UNICODE — Unicode casing

Input / fixture:

```text
José సాయి ß
```

Expected checks: {"exact": "JOSÉ సాయి SS"}

Recorded: SIMULATION / PASS

Native output:

```text
JOSÉ సాయి SS
```

Java type: java.lang.String; bytes: 21

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SCRIPT26_EMPTY — Empty body

Input / fixture:

```text
[empty body]
```

Expected checks: {"exact": ""}

Recorded: SIMULATION / PASS

Native output:

```text
[empty body]
```

Java type: java.lang.String; bytes: 0

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SCRIPT26_MULTI — Preserve line separators

Input / fixture:

```text
Line one
line two
```

Expected checks: {"exact": "LINE ONE\nLINE TWO"}

Recorded: SIMULATION / PASS

Native output:

```text
LINE ONE
LINE TWO
```

Java type: java.lang.String; bytes: 17

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SCRIPT26_JSON — Whole-body casing also changes JSON keys/values

Input / fixture:

```text
{"status":"new"}
```

Expected checks: {"exact": "{\"STATUS\":\"NEW\"}"}

Recorded: SIMULATION / PASS

Native output:

```text
{"STATUS":"NEW"}
```

Java type: java.lang.String; bytes: 16

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### JavaScript

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/Uppercase.js | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function | processData | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/LoadLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [821 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: []

# TRN 027 - XSLT Mapping

Technical ID: IF_TRN_027_XSLT_Mapping. Version 3.0.0.

Default Configure → ScenarioId=XSLT27_NAME. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| XSLT27_NAME | Template and value-of | base | SIMULATION: PASS |
| XSLT27_UNICODE | Unicode text | base | SIMULATION: PASS |
| XSLT27_ESCAPE | XML escaping | base | SIMULATION: PASS |
| XSLT27_MISSING | Missing name produces empty substitution | base | SIMULATION: PASS |
| XSLT27_BAD | Malformed XML rejected | base | SIMULATION: EXPECTED_REJECTION |

## XSLT27_NAME — Template and value-of

Input / fixture:

```text
<student><name>Anu</name></student>
```

Expected checks: {"xmlRoot": "greeting", "contains": ["Hello, Anu!"]}

Recorded: SIMULATION / PASS

Native output:

```text
<greeting>Hello, Anu!</greeting>
```

Java type: java.lang.String; bytes: 32

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XSLT27_UNICODE — Unicode text

Input / fixture:

```text
<student><name>José సాయి</name></student>
```

Expected checks: {"xmlRoot": "greeting", "contains": ["Hello, José సాయి!"]}

Recorded: SIMULATION / PASS

Native output:

```text
<greeting>Hello, José సాయి!</greeting>
```

Java type: java.lang.String; bytes: 47

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XSLT27_ESCAPE — XML escaping

Input / fixture:

```text
<student><name>A &amp; B</name></student>
```

Expected checks: {"xmlRoot": "greeting", "contains": ["Hello, A & B!"]}

Recorded: SIMULATION / PASS

Native output:

```text
<greeting>Hello, A &amp; B!</greeting>
```

Java type: java.lang.String; bytes: 38

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XSLT27_MISSING — Missing name produces empty substitution

Input / fixture:

```text
<student/>
```

Expected checks: {"xmlRoot": "greeting", "contains": ["Hello, !"]}

Recorded: SIMULATION / PASS

Native output:

```text
<greeting>Hello, !</greeting>
```

Java type: java.lang.String; bytes: 29

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XSLT27_BAD — Malformed XML rejected

Input / fixture:

```text
<student>
```

Expected checks: {"negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
com.ctc.wstx.exc.WstxEOFException: Unexpected EOF; was expecting a close tag for element <student>
 at [row,col {unknown-source}]: [1,9] on the exchange: Exchange[280904BA88A91F5-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### XSLT Mapping

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Source | mappingSrcIflow | Header, Integration Flow | When applicable |
| Processing | Resource | /mapping/Greeting.xslt | XSLT Mapping Resource | Conditional |
| Processing | Header Name |  | Provide mapping reference from header | Conditional |
| Processing | Output Format | String | String, Bytes | When applicable |

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/LoadLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [817 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: [{"severity": "Warning", "message": "Script may not pass XML message to XSLT Mapping. XSLT Mapping supports XML input only.", "elementId": "CallActivity_100", "elementType": "CallActivity", "location": "base - XSLT Mapping", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 027 - XSLT Mapping.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}]

# TRN 028 - XML Validator Valid

Technical ID: IF_TRN_028_XML_Validator_Valid. Version 3.0.0.

Default Configure → ScenarioId=VAL28_VALID. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| VAL28_VALID | Positive integer accepted | base | SIMULATION: PASS |
| VAL28_UNICODE | Unicode name accepted | base | SIMULATION: PASS |
| VAL28_TEXT | Text identifier rejected | base | SIMULATION: EXPECTED_REJECTION |
| VAL28_ZERO | Zero is not a positive integer | base | SIMULATION: EXPECTED_REJECTION |
| VAL28_MISSING | Required name missing | base | SIMULATION: EXPECTED_REJECTION |
| VAL28_ORDER | XSD sequence order enforced | base | SIMULATION: EXPECTED_REJECTION |

## VAL28_VALID — Positive integer accepted

Input / fixture:

```text
<student><id>1</id><name>Anu</name></student>
```

Expected checks: {"exact": "<student><id>1</id><name>Anu</name></student>"}

Recorded: SIMULATION / PASS

Native output:

```text
<student><id>1</id><name>Anu</name></student>
```

Java type: java.lang.String; bytes: 45

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## VAL28_UNICODE — Unicode name accepted

Input / fixture:

```text
<student><id>2</id><name>సాయి</name></student>
```

Expected checks: {"xmlRoot": "student", "contains": ["సాయి"]}

Recorded: SIMULATION / PASS

Native output:

```text
<student><id>2</id><name>సాయి</name></student>
```

Java type: java.lang.String; bytes: 54

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## VAL28_TEXT — Text identifier rejected

Input / fixture:

```text
<student><id>ABC</id><name>Anu</name></student>
```

Expected checks: {"negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
Validation failed for: /xsd/Student.xsd
errors: [
org.xml.sax.SAXParseException: The content "ABC" of element <id> does not match the required simple type. Cannot convert string "ABC" to an integer, Line : 1, Column : 14
]. Exchange[13E94D72F0556B8-0000000000000001] on the exchange: Exchange[13E94D72F0556B8-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## VAL28_ZERO — Zero is not a positive integer

Input / fixture:

```text
<student><id>0</id><name>Anu</name></student>
```

Expected checks: {"negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
Validation failed for: /xsd/Student.xsd
errors: [
org.xml.sax.SAXParseException: The content "0" of element <id> does not match the required simple type. Integer value is out of range for type xs:positiveInteger, Line : 1, Column : 14
]. Exchange[4591766705F1F67-0000000000000001] on the exchange: Exchange[4591766705F1F67-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## VAL28_MISSING — Required name missing

Input / fixture:

```text
<student><id>1</id></student>
```

Expected checks: {"negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
Validation failed for: /xsd/Student.xsd
errors: [
org.xml.sax.SAXParseException: In content of element <student>: The content is incomplete. Expected <Q{}name>. , Line : 1, Column : 10
]. Exchange[B47E866211B1492-0000000000000001] on the exchange: Exchange[B47E866211B1492-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## VAL28_ORDER — XSD sequence order enforced

Input / fixture:

```text
<student><name>Anu</name><id>1</id></student>
```

Expected checks: {"negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
Validation failed for: /xsd/Student.xsd
errors: [
org.xml.sax.SAXParseException: In content of element <student>: The content model does not allow element <Q{}name> to appear as the first child. It must be preceded by <Q{}id>. , Line : 1, Column : 16
]. Exchange[059591CE5100D77-0000000000000001] on the exchange: Exchange[059591CE5100D77-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### XML Validator

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Validation | Source | iflowOption | Header, Integration Flow | When applicable |
| Validation | Header Name |  | Enter a predefined header name without expressions. e.g. partnerXSDHeader | Conditional |
| Validation | XML Schema | /xsd/Student.xsd | Select the path where the XML Schema Definition (XSD) file is located. e.g. /xsd/myxsd.xsd | Conditional |
| Validation | Prevent Exception on Failure | false | Validation error details are added to the header (SAP_XmlValidationResult) and the processing is continued. | When applicable |

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/LoadLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [817 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: [{"severity": "Warning", "message": "Script may not pass XML message to XML Validator. XML Validator supports XML input only.", "elementId": "CallActivity_100", "elementType": "CallActivity", "location": "base - XML Validator", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 028 - XML Validator Valid.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}]

# TRN 029 - XML Validator Invalid

Technical ID: IF_TRN_029_XML_Validator_Invalid. Version 3.0.0.

Default Configure → ScenarioId=VAL29_TEXT. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| VAL29_TEXT | Text identifier rejected | base | SIMULATION: EXPECTED_REJECTION |
| VAL29_VALID | Positive integer accepted | base | SIMULATION: PASS |
| VAL29_UNICODE | Unicode name accepted | base | SIMULATION: PASS |
| VAL29_ZERO | Zero is not a positive integer | base | SIMULATION: EXPECTED_REJECTION |
| VAL29_MISSING | Required name missing | base | SIMULATION: EXPECTED_REJECTION |
| VAL29_ORDER | XSD sequence order enforced | base | SIMULATION: EXPECTED_REJECTION |

## VAL29_TEXT — Text identifier rejected

Input / fixture:

```text
<student><id>ABC</id><name>Anu</name></student>
```

Expected checks: {"negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
Validation failed for: /xsd/Student.xsd
errors: [
org.xml.sax.SAXParseException: The content "ABC" of element <id> does not match the required simple type. Cannot convert string "ABC" to an integer, Line : 1, Column : 14
]. Exchange[6D15A2C04285E78-0000000000000001] on the exchange: Exchange[6D15A2C04285E78-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## VAL29_VALID — Positive integer accepted

Input / fixture:

```text
<student><id>1</id><name>Anu</name></student>
```

Expected checks: {"exact": "<student><id>1</id><name>Anu</name></student>"}

Recorded: SIMULATION / PASS

Native output:

```text
<student><id>1</id><name>Anu</name></student>
```

Java type: java.lang.String; bytes: 45

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## VAL29_UNICODE — Unicode name accepted

Input / fixture:

```text
<student><id>2</id><name>సాయి</name></student>
```

Expected checks: {"xmlRoot": "student", "contains": ["సాయి"]}

Recorded: SIMULATION / PASS

Native output:

```text
<student><id>2</id><name>సాయి</name></student>
```

Java type: java.lang.String; bytes: 54

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## VAL29_ZERO — Zero is not a positive integer

Input / fixture:

```text
<student><id>0</id><name>Anu</name></student>
```

Expected checks: {"negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
Validation failed for: /xsd/Student.xsd
errors: [
org.xml.sax.SAXParseException: The content "0" of element <id> does not match the required simple type. Integer value is out of range for type xs:positiveInteger, Line : 1, Column : 14
]. Exchange[684F5A710D7EE6F-0000000000000001] on the exchange: Exchange[684F5A710D7EE6F-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## VAL29_MISSING — Required name missing

Input / fixture:

```text
<student><id>1</id></student>
```

Expected checks: {"negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
Validation failed for: /xsd/Student.xsd
errors: [
org.xml.sax.SAXParseException: In content of element <student>: The content is incomplete. Expected <Q{}name>. , Line : 1, Column : 10
]. Exchange[24860D55E5E0F54-0000000000000001] on the exchange: Exchange[24860D55E5E0F54-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## VAL29_ORDER — XSD sequence order enforced

Input / fixture:

```text
<student><name>Anu</name><id>1</id></student>
```

Expected checks: {"negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
Validation failed for: /xsd/Student.xsd
errors: [
org.xml.sax.SAXParseException: In content of element <student>: The content model does not allow element <Q{}name> to appear as the first child. It must be preceded by <Q{}id>. , Line : 1, Column : 16
]. Exchange[29388B999D901C0-0000000000000001] on the exchange: Exchange[29388B999D901C0-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### XML Validator

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Validation | Source | iflowOption | Header, Integration Flow | When applicable |
| Validation | Header Name |  | Enter a predefined header name without expressions. e.g. partnerXSDHeader | Conditional |
| Validation | XML Schema | /xsd/Student.xsd | Select the path where the XML Schema Definition (XSD) file is located. e.g. /xsd/myxsd.xsd | Conditional |
| Validation | Prevent Exception on Failure | false | Validation error details are added to the header (SAP_XmlValidationResult) and the processing is continued. | When applicable |

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/LoadLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [815 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: [{"severity": "Warning", "message": "Script may not pass XML message to XML Validator. XML Validator supports XML input only.", "elementId": "CallActivity_100", "elementType": "CallActivity", "location": "base - XML Validator", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 029 - XML Validator Invalid.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}]

# TRN 030 - CSV to XML

Technical ID: IF_TRN_030_CSV_to_XML. Version 3.0.0.

Default Configure → ScenarioId=CSV30_ROWS. Save and deploy once.

Configuration branches and overrides: {"base": {}, "header": {"overrides": {"0": {"ignoreFirstLineAsHeader": "true"}}}, "semi": {"overrides": {"0": {"Field_Separator_in_CSV": ";"}}}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| CSV30_ROWS | Two rows to XML | base | SIMULATION: PASS |
| CSV30_UNICODE | Unicode row | base | SIMULATION: PASS |
| CSV30_HEADER | Skip incoming header | header | SIMULATION: PASS |
| CSV30_SEMI | Semicolon input | semi | SIMULATION: PASS |
| CSV30_QUOTE | Quoted field contains delimiter | base | SIMULATION: PASS |
| CSV30_EMPTY | Empty field | base | SIMULATION: PASS |

## CSV30_ROWS — Two rows to XML

Input / fixture:

```text
1,Anu
2,Ravi
```

Expected checks: {"xmlRoot": "students", "xmlValues": {"id": ["1", "2"], "name": ["Anu", "Ravi"]}}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><students><student><id>1</id><name>Anu</name></student><student><id>2</id><name>Ravi</name></student></students>
```

Java type: [B; bytes: 150

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## CSV30_UNICODE — Unicode row

Input / fixture:

```text
3,José సాయి
```

Expected checks: {"xmlValues": {"name": ["José సాయి"]}}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><students><student><id>3</id><name>José సాయి</name></student></students>
```

Java type: [B; bytes: 119

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## CSV30_HEADER — Skip incoming header

Input / fixture:

```text
id,name
1,Anu
```

Expected checks: {"xmlValues": {"id": ["1"], "name": ["Anu"]}}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><students><student><id>1</id><name>Anu</name></student></students>
```

Java type: [B; bytes: 104

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## CSV30_SEMI — Semicolon input

Input / fixture:

```text
1;Anu
2;Ravi
```

Expected checks: {"xmlValues": {"id": ["1", "2"]}}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><students><student><id>1</id><name>Anu</name></student><student><id>2</id><name>Ravi</name></student></students>
```

Java type: [B; bytes: 150

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## CSV30_QUOTE — Quoted field contains delimiter

Input / fixture:

```text
4,"Anu, Ravi"
```

Expected checks: {"xmlValues": {"name": ["Anu, Ravi"]}}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><students><student><id>4</id><name>Anu, Ravi</name></student></students>
```

Java type: [B; bytes: 110

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## CSV30_EMPTY — Empty field

Input / fixture:

```text
5,
```

Expected checks: {"xmlRoot": "students"}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><students><student><id>5</id><name></name></student></students>
```

Java type: [B; bytes: 101

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### Router

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Throw Exception | false | Exception is thrown only if the sender channel is either IDoc or SOAP adapter, and the default route contains only Terminate End Event. | When applicable |
| Processing | conditionTable | [{"branchId": {"value": "SequenceFlow_200", "defaultValue": "", "key": ""}, "defaultBranch": {"value": "false", "defaultValue": "", "key": ""}, "conditionValue" … [1195 characters; full value in scenario/evidence JSON] |  | When applicable |
| Processing | Order |  |  | When applicable |
| Processing | Route Name |  |  | When applicable |
| Processing | Condition Expression |  |  | When applicable |
| Processing | Default Route |  |  | When applicable |

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### CSV To XML Converter

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | XML Schema | /xsd/Students.xsd | Select the path where the XML schema file is located | When applicable |
| Processing | Path to Target Element in XSD | /students/student | Enter the Path of the Record Identifier in XSD file<br>	Examples:<br>	1)/bookstore/book: Selects the book elements of the bookstore<br>	2)bookstore/book: Selects the book elements of the bookstore<br>	3)bookstore … [420 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | Record Marker in CSV |  | Enter Record Identifier in CSV file | When applicable |
| Processing | Field Separator in CSV | , | Tab(\t), Semicolon(;), Colon(:), Pipe(\|), Dot(.), Comma(,), Space | When applicable |
| Processing | Exclude First Line Header | true | Ignores first line of CSV file for conversion | When applicable |
| Processing | Configure CSV Headers to match | mapHeadersToXSD | CSV Fields Sequence, XSD Elements | Conditional |

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [815 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: [{"severity": "Warning", "message": "Script may not pass Text message to CSV To XML Converter. CSV To XML Converter supports Text input only.", "elementId": "CallActivity_160", "elementType": "CallActivity", "location": "semi - CSV To XML Converter", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 030 - CSV to XML.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}, {"severity": "Warning", "message": "Script may not pass Text message to CSV To XML Converter. CSV To XML Converter supports Text input only.", "elementId": "CallActivity_100", "elementType": "CallActivity", "location": "base - CSV To XML Converter", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 030 - CSV to XML.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}, {"severity": "Warning", "message": "Script may not pass Text message to CSV To XML Converter. CSV To XML Converter supports Text input only.", "elementId": " … [1265 characters; full value in scenario/evidence JSON]

# TRN 031 - Sequential Multicast

Technical ID: IF_TRN_031_Sequential_Multicast. Version 3.0.0.

Default Configure → ScenarioId=MC31_BASE. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| MC31_BASE | Two service responses | base | SIMULATION: PASS |
| MC31_UNICODE | Unicode branch responses | base | SIMULATION: PASS |
| MC31_EMPTY | Empty branch result | base | SIMULATION: PASS |
| MC31_INVALID | Sequential Gather rejects malformed branch XML | base | SIMULATION: EXPECTED_REJECTION |

## MC31_BASE — Two service responses

Input / fixture:

```text
<request/>
```

Properties: {"BranchA": "Sales", "BranchB": "Finance"}

Expected checks: {"xmlValues": {"result": ["Sales", "Finance"]}}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><multimap:Messages xmlns:multimap="http://sap.com/xi/XI/SplitAndMerge"><multimap:Message1><result>Sales</result><result>Finance</result></multimap:Message1></multimap:Messages>
```

Java type: [B; bytes: 214

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## MC31_UNICODE — Unicode branch responses

Input / fixture:

```text
<request/>
```

Properties: {"BranchA": "José", "BranchB": "సాయి"}

Expected checks: {"contains": ["José", "సాయి"]}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><multimap:Messages xmlns:multimap="http://sap.com/xi/XI/SplitAndMerge"><multimap:Message1><result>José</result><result>సాయి</result></multimap:Message1></multimap:Messages>
```

Java type: [B; bytes: 219

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## MC31_EMPTY — Empty branch result

Input / fixture:

```text
<request/>
```

Properties: {"BranchA": "", "BranchB": "Finance"}

Expected checks: {"contains": ["Finance"]}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version='1.0' encoding='UTF-8'?><multimap:Messages xmlns:multimap="http://sap.com/xi/XI/SplitAndMerge"><multimap:Message1><result/><result>Finance</result></multimap:Message1></multimap:Messages>
```

Java type: [B; bytes: 201

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## MC31_INVALID — Sequential Gather rejects malformed branch XML

Input / fixture:

```text
<request/>
```

Properties: {"BranchA": "A & B", "BranchB": "Finance"}

Expected checks: {"negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
com.ctc.wstx.exc.WstxUnexpectedCharException: Unexpected character ' ' (code 32) (missing name?)
 at [row,col {unknown-source}]: [1,12] on the exchange: Exchange[9949A6524A3D860-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | <result>${property.BranchA}</result> | Enter an expression or a constant to form new message body | When applicable |

### Gather

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Aggregation Strategy | Incoming Format | SameXMLFormat | Any, XML (Same Format), XML (Different Format), Plain Text | When applicable |
| Aggregation Strategy | Aggregation Algorithm | sap-identical-multi-mapping | Combine, Combine at XPath, Concatenate, Zip, Tar | When applicable |
| Aggregation Strategy | Combine from source (XPath) |  |  | Conditional |
| Aggregation Strategy | Combine at target (XPath) |  |  | Conditional |
| Aggregation Strategy | File Name |  | Configure a simple expression to define unique file names within the archive. If not specified, the header CamelFileName is evaluated. In case neither an expression is defined nor CamelFileName is ava … [255 characters; full value in scenario/evidence JSON] | Conditional |

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Join

No additional component processing fields; inspect General and the surrounding message flow.

### Sequential Multicast

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Routing Sequence | Specify the sequence of execution of branches | [{"branchName": {"value": "Branch A", "defaultValue": "", "key": ""}, "order": {"value": "1", "defaultValue": "", "key": ""}}, {"branchName": {"value": "Branch  … [252 characters; full value in scenario/evidence JSON] |  | When applicable |
| Routing Sequence | Sequence Name |  |  | When applicable |

Saved design messages: []

# TRN 032 - Looping Process Call

Technical ID: IF_TRN_032_Looping_Process_Call. Version 3.0.0.

Default Configure → ScenarioId=LOOP32_ZERO. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| LOOP32_ZERO | Repeat while below one | base | SIMULATION: PASS |
| LOOP32_ONE | Do-while executes once even when the initial counter is already one | base | SIMULATION: PASS |
| LOOP32_NEG | Local process sets counter to one | base | SIMULATION: PASS |
| LOOP32_BAD | Invalid XML condition rejected | base | SIMULATION: EXPECTED_REJECTION |

## LOOP32_ZERO — Repeat while below one

Input / fixture:

```text
<counter>0</counter>
```

Expected checks: {"xmlLeafValues": ["1"]}

Recorded: SIMULATION / PASS

Native output:

```text
<counter>1</counter>
```

Java type: java.lang.String; bytes: 20

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## LOOP32_ONE — Do-while executes once even when the initial counter is already one

Input / fixture:

```text
<counter>1</counter>
```

Expected checks: {"xmlLeafValues": ["2"]}

Recorded: SIMULATION / PASS

Native output:

```text
<counter>2</counter>
```

Java type: java.lang.String; bytes: 20

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## LOOP32_NEG — Local process sets counter to one

Input / fixture:

```text
<counter>-1</counter>
```

Expected checks: {"xmlLeafValues": ["1"]}

Recorded: SIMULATION / PASS

Native output:

```text
<counter>1</counter>
```

Java type: java.lang.String; bytes: 20

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## LOOP32_BAD — Invalid XML condition rejected

Input / fixture:

```text
<counter>
```

Expected checks: {"negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
java.lang.Exception: org.xml.sax.SAXParseException; lineNumber: 1; columnNumber: 10; XML document structures must start and end within the same entity.@ line 4 in IncrementCounter.groovy on the exchange: Exchange[97BAFB2D10536B6-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Event

No additional component processing fields; inspect General and the surrounding message flow.

### Start Event

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/IncrementCounter.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Looping Process Call

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Local Integration Process | Process_2 | Select a pre-defined Local Integration Process to be invoked | When applicable |
| Processing | Expression Type | XML | XML, Non-XML | When applicable |
| Processing | Condition Expression | /counter < 1 | Enter an expression to specify a routing condition. As long as the condition is true, loop processing is executed.<br>E.g. of XML conditions: /bank_name/bank_id = 'mybank', //BankId ='ABC'<br>E.g. of Non-XM … [239 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | Max. Numbers of Iterations | 3 | Specify the maximum number of iterations the loop should be executed. Even if the condition is still met the loop ends afterwards. | When applicable |
| Processing | Action when Max. Iterations Reached | throwException | End Loop, Throw Exception | When applicable |

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [817 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: []

# TRN 033 - Loop Maximum Guard

Technical ID: IF_TRN_033_Loop_Maximum_Guard. Version 3.0.0.

Default Configure → ScenarioId=LOOP33_GUARD. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| LOOP33_GUARD | Loop condition never becomes false: maximum guard fails | base | SIMULATION: EXPECTED_REJECTION |
| LOOP33_SKIP | Do-while executes once, then condition becomes false | base | SIMULATION: PASS |
| LOOP33_HIGH | Do-while still executes once above threshold | base | SIMULATION: PASS |
| LOOP33_ONE | Persistent condition still fails at maximum | base | SIMULATION: EXPECTED_REJECTION |

## LOOP33_GUARD — Loop condition never becomes false: maximum guard fails

Input / fixture:

```text
<counter>0</counter>
```

Properties: {"CounterLimit": 5}

Expected checks: {"negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
CallActivity_11 : Loop counter reached max. number of iterations on the exchange: Exchange[AA6D67DD70939A4-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## LOOP33_SKIP — Do-while executes once, then condition becomes false

Input / fixture:

```text
<counter>2</counter>
```

Properties: {"CounterLimit": 2}

Expected checks: {"xmlLeafValues": ["3"]}

Recorded: SIMULATION / PASS

Native output:

```text
<counter>3</counter>
```

Java type: java.lang.String; bytes: 20

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## LOOP33_HIGH — Do-while still executes once above threshold

Input / fixture:

```text
<counter>10</counter>
```

Properties: {"CounterLimit": 2}

Expected checks: {"xmlLeafValues": ["11"]}

Recorded: SIMULATION / PASS

Native output:

```text
<counter>11</counter>
```

Java type: java.lang.String; bytes: 21

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## LOOP33_ONE — Persistent condition still fails at maximum

Input / fixture:

```text
<counter>1</counter>
```

Properties: {"CounterLimit": 5}

Expected checks: {"negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
CallActivity_11 : Loop counter reached max. number of iterations on the exchange: Exchange[DDC7C45DB9554D9-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Event

No additional component processing fields; inspect General and the surrounding message flow.

### Start Event

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/IncrementCounter.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Looping Process Call

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Local Integration Process | Process_2 | Select a pre-defined Local Integration Process to be invoked | When applicable |
| Processing | Expression Type | Non XML | XML, Non-XML | When applicable |
| Processing | Condition Expression | ${property.CounterValue} < ${property.CounterLimit} | Enter an expression to specify a routing condition. As long as the condition is true, loop processing is executed.<br>E.g. of XML conditions: /bank_name/bank_id = 'mybank', //BankId ='ABC'<br>E.g. of Non-XM … [239 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | Max. Numbers of Iterations | 3 | Specify the maximum number of iterations the loop should be executed. Even if the condition is still met the loop ends afterwards. | When applicable |
| Processing | Action when Max. Iterations Reached | throwException | End Loop, Throw Exception | When applicable |

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [819 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: []

# TRN 034 - Idempotent Process Call

Technical ID: IF_TRN_034_Idempotent_Process_Call. Version 3.0.0.

Default Configure → ScenarioId=IDEMP34_FIRST. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| IDEMP34_FIRST | First occurrence executes local process | base | LIVE: PASS |
| IDEMP34_REPEAT | Repeat same key skips local call | base | LIVE: PASS |
| IDEMP34_NEW | Different key has its own first run | base | LIVE: PASS |

## IDEMP34_FIRST — First occurrence executes local process

Input / fixture:

```text
Hello from main
```

Properties: {"SourceId": "TRN034_REVIEW_A"}

Expected checks: {"oneOf": ["Hello from main", "Hello from local process"]}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_OutputSha256": "cSLRufb05CogJfYXmeWQeCiTyxR9SthI/anY4kFHdqA=", "TRN_TestResult": "PASS", "TRN_Result": "Hello from local process", "TRN_Expected": "COMPLETED", "TRN_OutputPreview": "Hello from local process", "TRN_ScenarioId": "IDEMP34_FIRST"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## IDEMP34_REPEAT — Repeat same key skips local call

Input / fixture:

```text
Hello from main
```

Properties: {"SourceId": "TRN034_REVIEW_A"}

Expected checks: {"oneOf": ["Hello from main", "Hello from local process"]}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_Expected": "COMPLETED", "TRN_OutputPreview": "Hello from main", "TRN_OutputSha256": "Exms7H4LAMiFF7uR4OaC0bChEYdeZcg/78P1BOsa13k=", "TRN_Result": "Hello from main", "TRN_TestResult": "PASS", "TRN_ScenarioId": "IDEMP34_REPEAT"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## IDEMP34_NEW — Different key has its own first run

Input / fixture:

```text
Hello from main
```

Properties: {"SourceId": "TRN034_REVIEW_B"}

Expected checks: {"oneOf": ["Hello from main", "Hello from local process"]}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_Result": "Hello from local process", "TRN_Expected": "COMPLETED", "TRN_ScenarioId": "IDEMP34_NEW", "TRN_OutputSha256": "cSLRufb05CogJfYXmeWQeCiTyxR9SthI/anY4kFHdqA=", "TRN_OutputPreview": "Hello from local process", "TRN_TestResult": "PASS"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Event

No additional component processing fields; inspect General and the surrounding message flow.

### Start Event

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | constant | Expression, Constant | When applicable |
| Message Body | Body | Hello from local process | Enter an expression or a constant to form new message body | When applicable |

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/LogTrainingResult.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Idempotent Process Call

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Local Integration Process | Process_2 | Select a pre-defined Local Integration Process to be invoked | When applicable |
| Processing | Message ID | ${property.SourceId} | Enter a unique message ID to check against. Use ${header.headername} or ${property.propertyname} to dynamically read the value from a header or a property. The uniqueness check happens for this flow s … [234 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | Skip Process Call for Duplicates | true | If selected, the local integration process will not run in case of a duplicate message. Use the property CamelDuplicateMessage=true to check if you are processing a duplicate. | When applicable |

Saved design messages: []

# TRN 035 - Exception Handled

Technical ID: IF_TRN_035_Exception_Handled. Version 3.0.0.

Default Configure → ScenarioId=EX35_TEXT. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| EX35_TEXT | Catch XML validation failure | base | LIVE: PASS |
| EX35_ZERO | Catch another schema error | base | LIVE: PASS |
| EX35_VALID | Valid input bypasses error handler | base | LIVE: PASS |
| EX35_XML | Catch malformed XML | base | LIVE: PASS |

## EX35_TEXT — Catch XML validation failure

Input / fixture:

```text
<student><id>ABC</id><name>Anu</name></student>
```

Expected checks: {"exact": "Handled validation error", "expectedRuntime": "COMPLETED", "handled": true}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_OutputSha256": "wZeGpuP9jsvtuswOuWRbiEVoR+TUc6n1W8cWwd9Yx6I=", "TRN_Expected": "COMPLETED", "TRN_ScenarioId": "EX35_TEXT", "TRN_OutputPreview": "Handled validation error", "TRN_TestResult": "PASS"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## EX35_ZERO — Catch another schema error

Input / fixture:

```text
<student><id>0</id><name>Anu</name></student>
```

Expected checks: {"exact": "Handled validation error", "expectedRuntime": "COMPLETED", "handled": true}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_TestResult": "PASS", "TRN_OutputPreview": "Handled validation error", "TRN_Expected": "COMPLETED", "TRN_OutputSha256": "wZeGpuP9jsvtuswOuWRbiEVoR+TUc6n1W8cWwd9Yx6I=", "TRN_ScenarioId": "EX35_ZERO"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## EX35_VALID — Valid input bypasses error handler

Input / fixture:

```text
<student><id>1</id><name>Anu</name></student>
```

Expected checks: {"exact": "<student><id>1</id><name>Anu</name></student>"}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_ScenarioId": "EX35_VALID", "TRN_TestResult": "PASS", "TRN_Expected": "COMPLETED", "TRN_OutputPreview": "<student><id>1</id><name>Anu</name></student>", "TRN_OutputSha256": "MIBhexjFOtM21nFXIFxm/fYgqhTreU2nDHW9YF2V2ec="}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## EX35_XML — Catch malformed XML

Input / fixture:

```text
<student>
```

Expected checks: {"exact": "Handled validation error", "handled": true, "expectedRuntime": "COMPLETED"}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_OutputSha256": "wZeGpuP9jsvtuswOuWRbiEVoR+TUc6n1W8cWwd9Yx6I=", "TRN_ScenarioId": "EX35_XML", "TRN_TestResult": "PASS", "TRN_OutputPreview": "Handled validation error", "TRN_Expected": "COMPLETED"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### XML Validator

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Validation | Source | iflowOption | Header, Integration Flow | When applicable |
| Validation | Header Name |  | Enter a predefined header name without expressions. e.g. partnerXSDHeader | Conditional |
| Validation | XML Schema | /xsd/Student.xsd | Select the path where the XML Schema Definition (XSD) file is located. e.g. /xsd/myxsd.xsd | Conditional |
| Validation | Prevent Exception on Failure | false | Validation error details are added to the header (SAP_XmlValidationResult) and the processing is continued. | When applicable |

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [833 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

### Exception Subprocess

No additional component processing fields; inspect General and the surrounding message flow.

### Error Start Event

No additional component processing fields; inspect General and the surrounding message flow.

Saved design messages: [{"severity": "Warning", "message": "Script may not pass XML message to XML Validator. XML Validator supports XML input only.", "elementId": "CallActivity_11", "elementType": "CallActivity", "location": "XmlValidator", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 035 - Exception Handled.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}]

# TRN 036 - Exception Rethrown

Technical ID: IF_TRN_036_Exception_Rethrown. Version 3.0.0.

Default Configure → ScenarioId=EX36_TEXT. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| EX36_TEXT | Catch XML validation failure | base | LIVE: PASS |
| EX36_ZERO | Catch another schema error | base | LIVE: PASS |
| EX36_VALID | Valid input bypasses error handler | base | LIVE: PASS |
| EX36_XML | Catch malformed XML | base | LIVE: PASS |

## EX36_TEXT — Catch XML validation failure

Input / fixture:

```text
<student><id>ABC</id><name>Anu</name></student>
```

Expected checks: {"exact": "Handled validation error", "expectedRuntime": "FAILED", "handled": true}

Recorded: LIVE / PASS; runtime FAILED

```text
b'XmlValidationException: Refer to MPL attachment(XML Validator Error Document) for details.'
```

Live checks: {"TRN_TestResult": "PASS", "TRN_OutputPreview": "Handled validation error", "TRN_OutputSha256": "wZeGpuP9jsvtuswOuWRbiEVoR+TUc6n1W8cWwd9Yx6I=", "TRN_ScenarioId": "EX36_TEXT", "TRN_Expected": "FAILED"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## EX36_ZERO — Catch another schema error

Input / fixture:

```text
<student><id>0</id><name>Anu</name></student>
```

Expected checks: {"exact": "Handled validation error", "expectedRuntime": "FAILED", "handled": true}

Recorded: LIVE / PASS; runtime FAILED

```text
b'XmlValidationException: Refer to MPL attachment(XML Validator Error Document) for details.'
```

Live checks: {"TRN_ScenarioId": "EX36_ZERO", "TRN_TestResult": "PASS", "TRN_OutputSha256": "wZeGpuP9jsvtuswOuWRbiEVoR+TUc6n1W8cWwd9Yx6I=", "TRN_Expected": "FAILED", "TRN_OutputPreview": "Handled validation error"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## EX36_VALID — Valid input bypasses error handler

Input / fixture:

```text
<student><id>1</id><name>Anu</name></student>
```

Expected checks: {"exact": "<student><id>1</id><name>Anu</name></student>"}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_TestResult": "PASS", "TRN_OutputPreview": "<student><id>1</id><name>Anu</name></student>", "TRN_ScenarioId": "EX36_VALID", "TRN_OutputSha256": "MIBhexjFOtM21nFXIFxm/fYgqhTreU2nDHW9YF2V2ec=", "TRN_Expected": "COMPLETED"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## EX36_XML — Catch malformed XML

Input / fixture:

```text
<student>
```

Expected checks: {"exact": "Handled validation error", "handled": true, "expectedRuntime": "FAILED"}

Recorded: LIVE / PASS; runtime FAILED

```text
b'XmlValidationException: Refer to MPL attachment(XML Validator Error Document) for details.'
```

Live checks: {"TRN_Expected": "FAILED", "TRN_OutputSha256": "wZeGpuP9jsvtuswOuWRbiEVoR+TUc6n1W8cWwd9Yx6I=", "TRN_ScenarioId": "EX36_XML", "TRN_TestResult": "PASS", "TRN_OutputPreview": "Handled validation error"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### XML Validator

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Validation | Source | iflowOption | Header, Integration Flow | When applicable |
| Validation | Header Name |  | Enter a predefined header name without expressions. e.g. partnerXSDHeader | Conditional |
| Validation | XML Schema | /xsd/Student.xsd | Select the path where the XML Schema Definition (XSD) file is located. e.g. /xsd/myxsd.xsd | Conditional |
| Validation | Prevent Exception on Failure | false | Validation error details are added to the header (SAP_XmlValidationResult) and the processing is continued. | When applicable |

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [833 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

### Exception Subprocess

No additional component processing fields; inspect General and the surrounding message flow.

### Error Start Event

No additional component processing fields; inspect General and the surrounding message flow.

### Error End Event

No additional component processing fields; inspect General and the surrounding message flow.

Saved design messages: [{"severity": "Warning", "message": "Script may not pass XML message to XML Validator. XML Validator supports XML input only.", "elementId": "CallActivity_11", "elementType": "CallActivity", "location": "XmlValidator", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 036 - Exception Rethrown.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}]

# TRN 037 - Error End Event

Technical ID: IF_TRN_037_Error_End_Event. Version 3.0.0.

Default Configure → ScenarioId=END37_TEXT. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| END37_TEXT | End-event status with text | base | LIVE: PASS |
| END37_XML | End-event status with XML | base | LIVE: PASS |
| END37_EMPTY | End-event status with empty body | base | LIVE: PASS |

## END37_TEXT — End-event status with text

Input / fixture:

```text
Order accepted
```

Expected checks: {"exact": "Order accepted", "expectedRuntime": "FAILED"}

Recorded: LIVE / PASS; runtime FAILED

```text
b'com.sap.esb.camel.error.handler.ErrorEventException: Error Event Exception'
```

Live checks: {"TRN_TestResult": "PASS", "TRN_Expected": "FAILED", "TRN_ScenarioId": "END37_TEXT", "TRN_OutputSha256": "8mmkLRpGmhS/smV5rV5U2JfZZJ3SmxDpds7nivh66eY=", "TRN_OutputPreview": "Order accepted"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## END37_XML — End-event status with XML

Input / fixture:

```text
<order>SO1001</order>
```

Expected checks: {"exact": "<order>SO1001</order>", "expectedRuntime": "FAILED"}

Recorded: LIVE / PASS; runtime FAILED

```text
b'com.sap.esb.camel.error.handler.ErrorEventException: Error Event Exception'
```

Live checks: {"TRN_OutputSha256": "UuTXEdLU73Q90WvMAFAFSvlOO0r51XJX8HVwJUKf8DI=", "TRN_TestResult": "PASS", "TRN_OutputPreview": "<order>SO1001</order>", "TRN_Expected": "FAILED", "TRN_ScenarioId": "END37_XML"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## END37_EMPTY — End-event status with empty body

Input / fixture:

```text
[empty body]
```

Expected checks: {"exact": "", "expectedRuntime": "FAILED"}

Recorded: LIVE / PASS; runtime FAILED

```text
b'com.sap.esb.camel.error.handler.ErrorEventException: Error Event Exception'
```

Live checks: {"TRN_ScenarioId": "END37_EMPTY", "TRN_OutputPreview": "", "TRN_OutputSha256": "47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU=", "TRN_Expected": "FAILED", "TRN_TestResult": "PASS"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### Error End Event

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [835 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: []

# TRN 038 - Escalation End Event

Technical ID: IF_TRN_038_Escalation_End_Event. Version 3.0.0.

Default Configure → ScenarioId=END38_TEXT. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| END38_TEXT | End-event status with text | base | LIVE: PASS |
| END38_XML | End-event status with XML | base | LIVE: PASS |
| END38_EMPTY | End-event status with empty body | base | LIVE: PASS |

## END38_TEXT — End-event status with text

Input / fixture:

```text
Order accepted
```

Expected checks: {"exact": "Order accepted", "expectedRuntime": "ESCALATED"}

Recorded: LIVE / PASS; runtime ESCALATED

Live checks: {"TRN_TestResult": "PASS", "TRN_OutputPreview": "Order accepted", "TRN_Expected": "ESCALATED", "TRN_OutputSha256": "8mmkLRpGmhS/smV5rV5U2JfZZJ3SmxDpds7nivh66eY=", "TRN_ScenarioId": "END38_TEXT"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## END38_XML — End-event status with XML

Input / fixture:

```text
<order>SO1001</order>
```

Expected checks: {"exact": "<order>SO1001</order>", "expectedRuntime": "ESCALATED"}

Recorded: LIVE / PASS; runtime ESCALATED

Live checks: {"TRN_OutputSha256": "UuTXEdLU73Q90WvMAFAFSvlOO0r51XJX8HVwJUKf8DI=", "TRN_ScenarioId": "END38_XML", "TRN_OutputPreview": "<order>SO1001</order>", "TRN_Expected": "ESCALATED", "TRN_TestResult": "PASS"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## END38_EMPTY — End-event status with empty body

Input / fixture:

```text
[empty body]
```

Expected checks: {"exact": "", "expectedRuntime": "ESCALATED"}

Recorded: LIVE / PASS; runtime ESCALATED

Live checks: {"TRN_TestResult": "PASS", "TRN_Expected": "ESCALATED", "TRN_ScenarioId": "END38_EMPTY", "TRN_OutputSha256": "47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU=", "TRN_OutputPreview": ""}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### Escalation End Event

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Category | ReceiverNotFound | Routing condition not met, Receiver not reachable, Receiver not found, Not authenticated to invoke receiver, Not authorized to invoke receiver, Receiver tries to redirect, Internal server error in rec … [237 characters; full value in scenario/evidence JSON] | When applicable |

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [835 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: []

# TRN 039 - Terminate Message

Technical ID: IF_TRN_039_Terminate_Message. Version 3.0.0.

Default Configure → ScenarioId=END39_TEXT. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| END39_TEXT | End-event status with text | base | LIVE: PASS |
| END39_XML | End-event status with XML | base | LIVE: PASS |
| END39_EMPTY | End-event status with empty body | base | LIVE: PASS |

## END39_TEXT — End-event status with text

Input / fixture:

```text
Order accepted
```

Expected checks: {"exact": "Order accepted", "expectedRuntime": "COMPLETED"}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_OutputSha256": "8mmkLRpGmhS/smV5rV5U2JfZZJ3SmxDpds7nivh66eY=", "TRN_OutputPreview": "Order accepted", "TRN_Expected": "COMPLETED", "TRN_ScenarioId": "END39_TEXT", "TRN_TestResult": "PASS"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## END39_XML — End-event status with XML

Input / fixture:

```text
<order>SO1001</order>
```

Expected checks: {"exact": "<order>SO1001</order>", "expectedRuntime": "COMPLETED"}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_OutputSha256": "UuTXEdLU73Q90WvMAFAFSvlOO0r51XJX8HVwJUKf8DI=", "TRN_TestResult": "PASS", "TRN_OutputPreview": "<order>SO1001</order>", "TRN_ScenarioId": "END39_XML", "TRN_Expected": "COMPLETED"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## END39_EMPTY — End-event status with empty body

Input / fixture:

```text
[empty body]
```

Expected checks: {"exact": "", "expectedRuntime": "COMPLETED"}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_Expected": "COMPLETED", "TRN_TestResult": "PASS", "TRN_ScenarioId": "END39_EMPTY", "TRN_OutputPreview": "", "TRN_OutputSha256": "47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU="}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Terminate Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [835 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: []

# TRN 040 - Plain End Event

Technical ID: IF_TRN_040_Plain_End_Event. Version 3.0.0.

Default Configure → ScenarioId=END40_TEXT. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| END40_TEXT | End-event status with text | base | LIVE: PASS |
| END40_XML | End-event status with XML | base | LIVE: PASS |
| END40_EMPTY | End-event status with empty body | base | LIVE: PASS |

## END40_TEXT — End-event status with text

Input / fixture:

```text
Order accepted
```

Expected checks: {"exact": "Order accepted", "expectedRuntime": "COMPLETED"}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_ScenarioId": "END40_TEXT", "TRN_OutputPreview": "Order accepted", "TRN_OutputSha256": "8mmkLRpGmhS/smV5rV5U2JfZZJ3SmxDpds7nivh66eY=", "TRN_Expected": "COMPLETED", "TRN_TestResult": "PASS"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## END40_XML — End-event status with XML

Input / fixture:

```text
<order>SO1001</order>
```

Expected checks: {"exact": "<order>SO1001</order>", "expectedRuntime": "COMPLETED"}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_TestResult": "PASS", "TRN_OutputSha256": "UuTXEdLU73Q90WvMAFAFSvlOO0r51XJX8HVwJUKf8DI=", "TRN_ScenarioId": "END40_XML", "TRN_OutputPreview": "<order>SO1001</order>", "TRN_Expected": "COMPLETED"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## END40_EMPTY — End-event status with empty body

Input / fixture:

```text
[empty body]
```

Expected checks: {"exact": "", "expectedRuntime": "COMPLETED"}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_ScenarioId": "END40_EMPTY", "TRN_OutputSha256": "47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU=", "TRN_Expected": "COMPLETED", "TRN_TestResult": "PASS", "TRN_OutputPreview": ""}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Event

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [835 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: []

# TRN 041 - ID Mapping

Technical ID: IF_TRN_041_ID_Mapping. Version 3.0.0.

Default Configure → ScenarioId=MAP41_FIRST. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| MAP41_FIRST | Map source A | base | LIVE: PASS |
| MAP41_REPEAT | Same source returns same target ID | base | LIVE: PASS |
| MAP41_NEW | Different source gets different target | base | LIVE: PASS |

## MAP41_FIRST — Map source A

Input / fixture:

```text
SourceA
```

Properties: {"SourceId": "TRN041_REVIEW_A"}

Expected checks: {"uuid": true}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_TestResult": "PASS", "TRN_OutputSha256": "KhbQdx1bxWL2Ju0rWCMtFU0yuh+sT5Xc93XsGsg+G3s=", "TRN_ScenarioId": "MAP41_FIRST", "TRN_Expected": "COMPLETED", "TRN_OutputPreview": "b8e02456-83ea-43c6-a14a-1d9966299743"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## MAP41_REPEAT — Same source returns same target ID

Input / fixture:

```text
SourceA
```

Properties: {"SourceId": "TRN041_REVIEW_A"}

Expected checks: {"uuid": true}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_OutputSha256": "KhbQdx1bxWL2Ju0rWCMtFU0yuh+sT5Xc93XsGsg+G3s=", "TRN_ScenarioId": "MAP41_REPEAT", "TRN_TestResult": "PASS", "TRN_Expected": "COMPLETED", "TRN_OutputPreview": "b8e02456-83ea-43c6-a14a-1d9966299743"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## MAP41_NEW — Different source gets different target

Input / fixture:

```text
SourceB
```

Properties: {"SourceId": "TRN041_REVIEW_B"}

Expected checks: {"uuid": true}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_Expected": "COMPLETED", "TRN_OutputSha256": "8Vpm5dBzAsrr1Ic8+5JU2Kp9xUrEfpuwXpCj6gxu/1U=", "TRN_ScenarioId": "MAP41_NEW", "TRN_OutputPreview": "e3a21101-8fa1-4478-85c1-89731bc77eeb", "TRN_TestResult": "PASS"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${header.MappedId} | Enter an expression or a constant to form new message body | When applicable |

### ID Mapping

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Source Message ID | ${property.SourceId} | Enter a message ID that is to be mapped to a target ID. Use ${header.headername} or ${property.propertyname} to dynamically read the value from a header or a property. | When applicable |
| Processing | Target Header Name | MappedId | Enter a target header name in which to store the mapped ID. | When applicable |
| Processing | Context | TRN041_CONTEXT | Enter a Context which will allow you to share the same ID mapping across multiple ID mapping steps. You can also enter ${header.headername} or ${property.propertyname} to dynamically read the value fr … [226 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | Visibility | local | Integration Flow, Global | When applicable |
| Processing | Expiration Period (in d) | 2 | Enter a time period (in days). The mapping is automatically deleted after this period. The default value is 30 days. | When applicable |

Saved design messages: []

# TRN 042 - Write Variables

Technical ID: IF_TRN_042_Write_Variables. Version 3.0.0.

Default Configure → ScenarioId=VAR42_TEXT. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| VAR42_TEXT | Write/read local variable | base | LIVE: PASS |
| VAR42_UNICODE | Persist Unicode | base | LIVE: PASS |
| VAR42_JSON | Persist structured text | base | LIVE: PASS |
| VAR42_REPLACE | Overwrite own variable | base | LIVE: PASS |

## VAR42_TEXT — Write/read local variable

Input / fixture:

```text
variable demo
```

Properties: {"PersistedGreeting": "Hello from a persisted variable"}

Expected checks: {"exact": "Hello from a persisted variable"}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_ScenarioId": "VAR42_TEXT", "TRN_OutputSha256": "ycq4Y8O4oxC+965AwheXSpxx6AKitc/r+aptRMnsVqE=", "TRN_Expected": "COMPLETED", "TRN_OutputPreview": "Hello from a persisted variable", "TRN_TestResult": "PASS"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## VAR42_UNICODE — Persist Unicode

Input / fixture:

```text
variable demo
```

Properties: {"PersistedGreeting": "Hello సాయి"}

Expected checks: {"exact": "Hello సాయి"}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_ScenarioId": "VAR42_UNICODE", "TRN_Expected": "COMPLETED", "TRN_TestResult": "PASS", "TRN_OutputSha256": "YPXd24uC4lu0hnHnsSg/bCMFD0HIJHDwe9kZhLTzO3A=", "TRN_OutputPreview": "Hello సాయి"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## VAR42_JSON — Persist structured text

Input / fixture:

```text
variable demo
```

Properties: {"PersistedGreeting": "{\"status\":\"accepted\"}"}

Expected checks: {"exact": "{\"status\":\"accepted\"}"}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_OutputSha256": "C6fANWFJlGvwZC+rTvhblecJD294XQ+4QyPQxELlGQo=", "TRN_Expected": "COMPLETED", "TRN_ScenarioId": "VAR42_JSON", "TRN_OutputPreview": "{\"status\":\"accepted\"}", "TRN_TestResult": "PASS"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## VAR42_REPLACE — Overwrite own variable

Input / fixture:

```text
variable demo
```

Properties: {"PersistedGreeting": "Updated greeting"}

Expected checks: {"exact": "Updated greeting"}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_OutputSha256": "WCX0wHZY319dFYSmiCL8B6dDRVIQpKK2QoSvm+h24hU=", "TRN_TestResult": "PASS", "TRN_ScenarioId": "VAR42_REPLACE", "TRN_Expected": "COMPLETED", "TRN_OutputPreview": "Updated greeting"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "persisted variables", "defaultValue": "", "key": ""}, "Value": {"value": "TRN … [404 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | constant | Expression, Constant | When applicable |
| Message Body | Body |  | Enter an expression or a constant to form new message body | When applicable |

### Write Variables

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Variables | [{"datatype": {"value": "java.lang.String", "defaultValue": "", "key": ""}, "scope": {"value": "false", "defaultValue": "", "key": ""}, "name": {"value": "TRN04 … [339 characters; full value in scenario/evidence JSON] |  | When applicable |
| Processing | Name |  | Enter a property name name to read variable value | When applicable |
| Processing | Type |  | Constant, Header, Xpath, Expression, Property | When applicable |
| Processing | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Processing | Value |  | Enter variable key | When applicable |
| Processing | Global Scope |  | Enter data store scope local to Integration Flow or tenant | When applicable |

Saved design messages: []

# TRN 043 - Data Store Select

Technical ID: IF_TRN_043_Data_Store_Select. Version 3.0.0.

Default Configure → ScenarioId=SELECT43_TWO. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| SELECT43_TWO | Select two dedicated records | base | LIVE: PASS |
| SELECT43_UNICODE | Select Unicode records | base | LIVE: PASS |
| SELECT43_NUMERIC | Select numeric-looking text | base | LIVE: PASS |

## SELECT43_TWO — Select two dedicated records

Input / fixture:

```text
<record>One</record>
```

Properties: {"SecondRecord": "<record>Two</record>"}

Expected checks: {"storedValues": ["One", "Two"]}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_OutputSha256": "F35TXBJbPsOUbMaT1nKyW1QqdIlpi2PoeOhIr1PgYDc=", "TRN_ScenarioId": "SELECT43_TWO", "TRN_TestResult": "PASS", "TRN_Expected": "COMPLETED", "TRN_OutputPreview": "<?xml version='1.0' encoding='UTF-8'?><messages><message id=\"ONE\"><record>One</record></message><message id=\"TWO\"><record>Two</record></message></messages>"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SELECT43_UNICODE — Select Unicode records

Input / fixture:

```text
<record>José</record>
```

Properties: {"SecondRecord": "<record>సాయి</record>"}

Expected checks: {"storedValues": ["José", "సాయి"]}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_Expected": "COMPLETED", "TRN_OutputSha256": "DYE76VtWivrrluBjCawFI9DzlSHvYXioeKeuhKO7cVk=", "TRN_ScenarioId": "SELECT43_UNICODE", "TRN_TestResult": "PASS", "TRN_OutputPreview": "<?xml version='1.0' encoding='UTF-8'?><messages><message id=\"ONE\"><record>José</record></message><message id=\"TWO\"><record>సాయి</record></message></messages>"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SELECT43_NUMERIC — Select numeric-looking text

Input / fixture:

```text
<record>1001</record>
```

Properties: {"SecondRecord": "<record>1002</record>"}

Expected checks: {"storedValues": ["1001", "1002"]}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_OutputPreview": "<?xml version='1.0' encoding='UTF-8'?><messages><message id=\"ONE\"><record>1001</record></message><message id=\"TWO\"><record>1002</record></message></messages>", "TRN_Expected": "COMPLETED", "TRN_TestResult": "PASS", "TRN_OutputSha256": "aUSYYEi0XKZZ5ZitYMAYxw0h1viyKdhKZOlaoW8/ajA=", "TRN_ScenarioId": "SELECT43_NUMERIC"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.SecondRecord} | Enter an expression or a constant to form new message body | When applicable |

### Write

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Data Store Name | TRN043_BATCH | Enter a data store name (maximum 40 characters). The name should begin with an alphabetic character and use characters (aA-zZ, 0-9, - _ . ~ ) without any whitespace. You can also enter ${header.header … [298 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | Visibility | local | Integration Flow, Global | When applicable |
| Processing | Entry ID | ONE | Entry id with which content will be persisted, you can use ${header.headername}, ${property.propertyname} or ${xpath.node} to read entry id details from incoming message. A generated GUID would be use … [223 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | Retention Threshold for Alerting (in d) | 1 | Enter a time period (in days). If messages are not fetched within this period, an alert is raised. The default value is 2. | When applicable |
| Processing | Expiration Period (in d) | 2 | Enter a time period (in days). The stored message is automatically deleted from the data store after this period. The default value is 30 days. | When applicable |
| Processing | Encrypt Stored Message | true | Select this option to encrypt the message in data store | When applicable |
| Processing | Overwrite Existing Message | true | Select this option to overwrite the message in data store | When applicable |
| Processing | Include Message Headers | false | Select this option to also store the message's headers | When applicable |

### Select

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Data Store Name | TRN043_BATCH | Enter a data store name (maximum 40 characters). The name should begin with an alphabetic character and use characters (aA-zZ, 0-9, - _ . ~ ) without any whitespace. You can also enter ${header.header … [298 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | Visibility | local | Integration Flow, Global | When applicable |
| Processing | Number of Polled Messages | 2 | Specify the number of messages to be fetched from data store in one poll. Default value is 1. If messages fetched per poll is also specified in the header 'SapDataStoreMaxResults', the header takes th … [318 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | Delete On Completion | true | Select this option to delete messages from data store after successfully processing the messages | When applicable |

Saved design messages: []

# TRN 044 - ZIP Splitter

Technical ID: IF_TRN_044_ZIP_Splitter. Version 3.0.0.

Default Configure → ScenarioId=ARC44_TWO. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| ARC44_TWO | Two file archive | base | SIMULATION: PASS |
| ARC44_ONE | One file archive | base | SIMULATION: PASS |
| ARC44_UTF8 | UTF-8 file contents | base | SIMULATION: PASS |
| ARC44_EMPTYFILE | An empty file plus a normal file | base | SIMULATION: PASS |
| ARC44_BAD | Invalid archive rejected | base | SIMULATION: EXPECTED_REJECTION |

## ARC44_TWO — Two file archive

Input / fixture:

```text
UEsDBBQAAAAAAFk1cV2LntnTAQAAAAEAAAAFAAAAYS50eHRBUEsDBBQAAAAAAFk1cV0xz9BKAQAAAAEAAAAFAAAAYi50eHRCUEsBAhQDFAAAAAAAWTVxXYue2dMBAAAAAQAAAAUAAAAAAAAAAAAAAIABAAAAAGEudHh0UEsBAhQDFAAAAAAAWTVxXTHP0EoBAAAAAQAAAAUAAAAAAAAAAAAAAIABJAAAAGIudHh0UEsFBgAAAAACAAIAZgAAAEgAAAAAAA==
```

Expected checks: {"exact": "AB"}

Recorded: SIMULATION / PASS

Native output:

```text
AB
```

Java type: [B; bytes: 2

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## ARC44_ONE — One file archive

Input / fixture:

```text
UEsDBBQAAAAAAFk1cV0RwCFCAwAAAAMAAAAHAAAAb25lLnR4dE9uZVBLAQIUAxQAAAAAAFk1cV0RwCFCAwAAAAMAAAAHAAAAAAAAAAAAAACAAQAAAABvbmUudHh0UEsFBgAAAAABAAEANQAAACgAAAAAAA==
```

Expected checks: {"exact": "One"}

Recorded: SIMULATION / PASS

Native output:

```text
One
```

Java type: [B; bytes: 3

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## ARC44_UTF8 — UTF-8 file contents

Input / fixture:

```text
UEsDBBQAAAAAAFk1cV1oGU6uBQAAAAUAAAAFAAAAYS50eHRKb3PDqVBLAwQUAAAAAABZNXFduNkSaAwAAAAMAAAABQAAAGIudHh04LC44LC+4LCv4LC/UEsBAhQDFAAAAAAAWTVxXWgZTq4FAAAABQAAAAUAAAAAAAAAAAAAAIABAAAAAGEudHh0UEsBAhQDFAAAAAAAWTVxXbjZEmgMAAAADAAAAAUAAAAAAAAAAAAAAIABKAAAAGIudHh0UEsFBgAAAAACAAIAZgAAAFcAAAAAAA==
```

Expected checks: {"exact": "Joséసాయి"}

Recorded: SIMULATION / PASS

Native output:

```text
Joséసాయి
```

Java type: [B; bytes: 17

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## ARC44_EMPTYFILE — An empty file plus a normal file

Input / fixture:

```text
UEsDBBQAAAAAAFk1cV0AAAAAAAAAAAAAAAAFAAAAYS50eHRQSwMEFAAAAAAAWTVxXTHP0EoBAAAAAQAAAAUAAABiLnR4dEJQSwECFAMUAAAAAABZNXFdAAAAAAAAAAAAAAAABQAAAAAAAAAAAAAAgAEAAAAAYS50eHRQSwECFAMUAAAAAABZNXFdMc/QSgEAAAABAAAABQAAAAAAAAAAAAAAgAEjAAAAYi50eHRQSwUGAAAAAAIAAgBmAAAARwAAAAAA
```

Expected checks: {"exact": "B"}

Recorded: SIMULATION / PASS

Native output:

```text
B
```

Java type: [B; bytes: 1

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## ARC44_BAD — Invalid archive rejected

Input / fixture:

```text
bm90IGFuIGFyY2hpdmU=
```

Expected checks: {"negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
Unable to unzip the file, it may be corrupted. on the exchange: Exchange[B6EC10D755B33EC-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Zip Splitter

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | File Name Pattern |  | Configure a file name pattern if not all files from the zip archive are to be processed, for example using *.xml to process only the xml files. The other files from the archive are not further process … [267 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | Stop on Exception | true | You have the option to stop message processing immediately if an exception occurs.<br>If you disable this option, message splitting and processing of the submessages continues, even if one of them fails. | When applicable |

### Gather

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Aggregation Strategy | Incoming Format | PlainText | Any, XML (Same Format), XML (Different Format), Plain Text | When applicable |
| Aggregation Strategy | Aggregation Algorithm | concatenate-strategy | Combine, Combine at XPath, Concatenate, Zip, Tar | When applicable |
| Aggregation Strategy | Combine from source (XPath) |  |  | Conditional |
| Aggregation Strategy | Combine at target (XPath) |  |  | Conditional |
| Aggregation Strategy | File Name |  | Configure a simple expression to define unique file names within the archive. If not specified, the header CamelFileName is evaluated. In case neither an expression is defined nor CamelFileName is ava … [255 characters; full value in scenario/evidence JSON] | Conditional |

### Base64 Decode

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [813 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: [{"severity": "Warning", "message": "Script may not pass Base64Encoded message to Decoder/Base64 Decode. Decoder/Base64 Decode supports Base64Encoded input only.", "elementId": "CallActivity_100", "elementType": "CallActivity", "attributeId": "iflow.advance.decoder.tab", "location": "base - Base64 Decode", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 044 - ZIP Splitter.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}]

# TRN 045 - Tar Splitter

Technical ID: IF_TRN_045_Tar_Splitter. Version 3.0.0.

Default Configure → ScenarioId=ARC45_TWO. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| ARC45_TWO | Two file archive | base | SIMULATION: PASS |
| ARC45_ONE | One file archive | base | SIMULATION: PASS |
| ARC45_UTF8 | UTF-8 file contents | base | SIMULATION: PASS |
| ARC45_EMPTYFILE | An empty file plus a normal file | base | SIMULATION: PASS |
| ARC45_BAD | Invalid archive rejected | base | SIMULATION: EXPECTED_REJECTION |

## ARC45_TWO — Two file archive

Input / fixture:

```text
YS50eHQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADAwMDA2NDQAMDAwMDAwMAAwMDAwMDAwADAwMDAwMDAwMDAxADAwMDAwMDAwMDAwADAwNjcxNQAgMAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB1c3RhcgAwMAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA … [13656 characters; full value in scenario/evidence JSON]
```

Expected checks: {"exact": "AB"}

Recorded: SIMULATION / PASS

Native output:

```text
AB
```

Java type: [B; bytes: 2

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## ARC45_ONE — One file archive

Input / fixture:

```text
b25lLnR4dAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADAwMDA2NDQAMDAwMDAwMAAwMDAwMDAwADAwMDAwMDAwMDAzADAwMDAwMDAwMDAwADAwNzI2MAAgMAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB1c3RhcgAwMAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABPbmUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA … [13656 characters; full value in scenario/evidence JSON]
```

Expected checks: {"exact": "One"}

Recorded: SIMULATION / PASS

Native output:

```text
One
```

Java type: [B; bytes: 3

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## ARC45_UTF8 — UTF-8 file contents

Input / fixture:

```text
YS50eHQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADAwMDA2NDQAMDAwMDAwMAAwMDAwMDAwADAwMDAwMDAwMDA1ADAwMDAwMDAwMDAwADAwNjcyMQAgMAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB1c3RhcgAwMAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABKb3PDqQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA … [13656 characters; full value in scenario/evidence JSON]
```

Expected checks: {"exact": "Joséసాయి"}

Recorded: SIMULATION / PASS

Native output:

```text
Joséసాయి
```

Java type: [B; bytes: 17

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## ARC45_EMPTYFILE — An empty file plus a normal file

Input / fixture:

```text
YS50eHQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADAwMDA2NDQAMDAwMDAwMAAwMDAwMDAwADAwMDAwMDAwMDAwADAwMDAwMDAwMDAwADAwNjcxNAAgMAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB1c3RhcgAwMAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABiLnR4dAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMDAwMDY0NAAwMDAwMDAwADAwMDAwMDAAMDAwMDAwMDAwMDEAMDAwMDAwMDAwMDAAMDA2NzE2ACAwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHVzdGFyADAwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA … [13656 characters; full value in scenario/evidence JSON]
```

Expected checks: {"exact": "B"}

Recorded: SIMULATION / PASS

Native output:

```text
B
```

Java type: [B; bytes: 1

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## ARC45_BAD — Invalid archive rejected

Input / fixture:

```text
bm90IGFuIGFyY2hpdmU=
```

Expected checks: {"negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
Unable to untar the file, it may be corrupted. on the exchange: Exchange[EB5A41523C6F21A-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Tar Splitter

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | File Name Pattern |  | Configure a file name pattern if not all files from the tar archive are to be processed, for example using *.xml to process only the xml files. The other files from the archive are not further process … [267 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | Stop on Exception | true | You have the option to stop message processing immediately if an exception occurs.<br>If you disable this option, message splitting and processing of the submessages continues, even if one of them fails. | When applicable |

### Gather

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Aggregation Strategy | Incoming Format | PlainText | Any, XML (Same Format), XML (Different Format), Plain Text | When applicable |
| Aggregation Strategy | Aggregation Algorithm | concatenate-strategy | Combine, Combine at XPath, Concatenate, Zip, Tar | When applicable |
| Aggregation Strategy | Combine from source (XPath) |  |  | Conditional |
| Aggregation Strategy | Combine at target (XPath) |  |  | Conditional |
| Aggregation Strategy | File Name |  | Configure a simple expression to define unique file names within the archive. If not specified, the header CamelFileName is evaluated. In case neither an expression is defined nor CamelFileName is ava … [255 characters; full value in scenario/evidence JSON] | Conditional |

### Base64 Decode

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [813 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: [{"severity": "Warning", "message": "Script may not pass Base64Encoded message to Decoder/Base64 Decode. Decoder/Base64 Decode supports Base64Encoded input only.", "elementId": "CallActivity_100", "elementType": "CallActivity", "attributeId": "iflow.advance.decoder.tab", "location": "base - Base64 Decode", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 045 - Tar Splitter.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}]

# TRN 046 - Headers Properties

Technical ID: IF_TRN_046_Headers_Properties. Version 3.0.0.

Default Configure → ScenarioId=HP46_DEFAULT. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| HP46_DEFAULT | Use header and exchange property | base | SIMULATION: PASS |
| HP46_OTHER | Change course header | base | SIMULATION: PASS |
| HP46_UNICODE | Unicode property | base | SIMULATION: PASS |
| HP46_EMPTY | Empty optional student name | base | SIMULATION: PASS |

## HP46_DEFAULT — Use header and exchange property

Input / fixture:

```text
original
```

Properties: {"StudentName": "Anu"}

Expected checks: {"inputHeaders": {"Course": "CPI"}, "exact": "Hello Anu, welcome to CPI!"}

Recorded: SIMULATION / PASS

Native output:

```text
Hello Anu, welcome to CPI!
```

Java type: java.lang.String; bytes: 26

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## HP46_OTHER — Change course header

Input / fixture:

```text
original
```

Properties: {"StudentName": "Ravi"}

Expected checks: {"inputHeaders": {"Course": "Integration Suite"}, "exact": "Hello Ravi, welcome to Integration Suite!"}

Recorded: SIMULATION / PASS

Native output:

```text
Hello Ravi, welcome to Integration Suite!
```

Java type: java.lang.String; bytes: 41

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## HP46_UNICODE — Unicode property

Input / fixture:

```text
original
```

Properties: {"StudentName": "సాయి"}

Expected checks: {"inputHeaders": {"Course": "CPI"}, "exact": "Hello సాయి, welcome to CPI!"}

Recorded: SIMULATION / PASS

Native output:

```text
Hello సాయి, welcome to CPI!
```

Java type: java.lang.String; bytes: 35

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## HP46_EMPTY — Empty optional student name

Input / fixture:

```text
original
```

Properties: {"StudentName": ""}

Expected checks: {"inputHeaders": {"Course": "CPI"}, "exact": "Hello , welcome to CPI!"}

Recorded: SIMULATION / PASS

Native output:

```text
Hello , welcome to CPI!
```

Java type: java.lang.String; bytes: 23

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "header", "defaultValue": "", "key": ""}, "Value": {"value": "Course", "defaul … [376 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "property", "defaultValue": "", "key": ""}, "Value": {"value": "StudentName",  … [388 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | Hello ${property.StudentName}, welcome to ${header.Course}! | Enter an expression or a constant to form new message body | When applicable |

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/LoadLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

Saved design messages: []

# TRN 047 - XPath Property

Technical ID: IF_TRN_047_XPath_Property. Version 3.0.0.

Default Configure → ScenarioId=XP47_NAME. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| XP47_NAME | XPath property used in expression | base | SIMULATION: PASS |
| XP47_UTF8 | Unicode XPath value | base | SIMULATION: PASS |
| XP47_MISSING | Missing optional XPath name | base | SIMULATION: OBSERVED |
| XP47_BAD | Malformed XML rejected | base | SIMULATION: EXPECTED_REJECTION |

## XP47_NAME — XPath property used in expression

Input / fixture:

```text
<student><name>Anu</name></student>
```

Expected checks: {"exact": "Hello Anu!"}

Recorded: SIMULATION / PASS

Native output:

```text
Hello Anu!
```

Java type: java.lang.String; bytes: 10

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XP47_UTF8 — Unicode XPath value

Input / fixture:

```text
<student><name>సాయి</name></student>
```

Expected checks: {"exact": "Hello సాయి!"}

Recorded: SIMULATION / PASS

Native output:

```text
Hello సాయి!
```

Java type: java.lang.String; bytes: 19

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XP47_MISSING — Missing optional XPath name

Input / fixture:

```text
<student/>
```

Expected checks: {"observe": true}

Recorded: SIMULATION / OBSERVED

Native output:

```text
Hello !
```

Java type: java.lang.String; bytes: 7

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## XP47_BAD — Malformed XML rejected

Input / fixture:

```text
<student>
```

Expected checks: {"negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
javax.xml.xpath.XPathExpressionException: Failure converting a node of class javax.xml.transform.sax.SAXSource: org.xml.sax.SAXParseException; lineNumber: 1; columnNumber: 10; XML document structures must start and end within the same entity. on the exchange: Exchange[B66292D6835F948-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | Hello ${property.StudentName}! | Enter an expression or a constant to form new message body | When applicable |

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

Saved design messages: []

# TRN 048 - Request Reply

Technical ID: IF_TRN_048_Request_Reply. Version 3.0.0.

Default Configure → ScenarioId=CALL48_MATCH. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| CALL48_MATCH | Matching lookup id | base | LIVE: PASS |
| CALL48_UTF8 | Unicode request preserved | base | LIVE: PASS |
| CALL48_NOMATCH | No matching lookup key | base | LIVE: PASS |
| CALL48_MULTI | Multiple request records | base | LIVE: PASS |
| CALL48_EMPTY | Empty lookup request returns an empty lookup | base | LIVE: PASS |
| CALL48_BAD | Receiver rejects malformed XML | base | LIVE: PASS |

## CALL48_MATCH — Matching lookup id

Input / fixture:

```text
<students><student><id>1</id><name>Anu</name></student></students>
```

Expected checks: {"contains": ["Sales"], "receiverCall": true}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_ScenarioId": "CALL48_MATCH", "TRN_TestResult": "PASS", "TRN_OutputPreview": "<lookup><department><id>1</id><name>Sales</name></department></lookup>", "TRN_Expected": "COMPLETED", "TRN_OutputSha256": "xkt/0F+b9OaptOQjCFRpa0hrWajY9+jj38hV3rtnoKM="}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## CALL48_UTF8 — Unicode request preserved

Input / fixture:

```text
<students><student><id>1</id><name>సాయి</name></student></students>
```

Expected checks: {"contains": ["Sales"], "receiverCall": true}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_TestResult": "PASS", "TRN_OutputPreview": "<lookup><department><id>1</id><name>Sales</name></department></lookup>", "TRN_OutputSha256": "xkt/0F+b9OaptOQjCFRpa0hrWajY9+jj38hV3rtnoKM=", "TRN_Expected": "COMPLETED", "TRN_ScenarioId": "CALL48_UTF8"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## CALL48_NOMATCH — No matching lookup key

Input / fixture:

```text
<students><student><id>999</id><name>Anu</name></student></students>
```

Expected checks: {"receiverCall": true, "observe": true}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_OutputPreview": "<lookup></lookup>", "TRN_OutputSha256": "gGT2agNxBsQthMKjLluxleTLLVUm/BIumn93wq88FKw=", "TRN_Expected": "COMPLETED", "TRN_ScenarioId": "CALL48_NOMATCH", "TRN_TestResult": "OBSERVED"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## CALL48_MULTI — Multiple request records

Input / fixture:

```text
<students><student><id>1</id><name>Anu</name></student><student><id>1</id><name>Ravi</name></student></students>
```

Expected checks: {"contains": ["Sales"], "receiverCall": true}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_OutputSha256": "xkt/0F+b9OaptOQjCFRpa0hrWajY9+jj38hV3rtnoKM=", "TRN_OutputPreview": "<lookup><department><id>1</id><name>Sales</name></department></lookup>", "TRN_Expected": "COMPLETED", "TRN_TestResult": "PASS", "TRN_ScenarioId": "CALL48_MULTI"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## CALL48_EMPTY — Empty lookup request returns an empty lookup

Input / fixture:

```text
<students/>
```

Expected checks: {"observe": true, "receiverCall": true}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_OutputPreview": "<lookup></lookup>", "TRN_Expected": "COMPLETED", "TRN_ScenarioId": "CALL48_EMPTY", "TRN_OutputSha256": "gGT2agNxBsQthMKjLluxleTLLVUm/BIumn93wq88FKw=", "TRN_TestResult": "OBSERVED"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## CALL48_BAD — Receiver rejects malformed XML

Input / fixture:

```text
<students>
```

Expected checks: {"negative": true, "receiverCall": true}

Recorded: LIVE / PASS; runtime FAILED

```text
b'javax.script.ScriptException: java.lang.Exception: org.xml.sax.SAXParseException; lineNumber: 1; columnNumber: 11; XML document structures must start and end within the same entity.@ line 13 in MockLookup.groovy, cause: org.xml.sax.SAXParseException; lineNumber: 1; columnNumber: 11; XML document structures must start and end within the same entity.'
```

Live checks: {"TRN_ScenarioId": "CALL48_BAD", "TRN_Expected": "FAILED"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Request Reply

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [839 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: []

# TRN 049 - Local Mock Receiver

Technical ID: IF_TRN_049_Local_Mock_Receiver. Version 3.0.0.

ProcessDirect receiver: deploy before its caller; output remains usable by the caller.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| MOCK49_SALES | Sales lookup request | base | LIVE_CALLER: PASS |
| MOCK49_UNKNOWN | Unknown key response | base | LIVE_CALLER: PASS |
| MOCK49_EMPTY | Empty request response | base | LIVE_CALLER: PASS |

## MOCK49_SALES — Sales lookup request

Input / fixture:

```text
<students><student><id>1</id></student></students>
```

Expected checks: {"contains": ["Sales"]}

Recorded: LIVE_CALLER / PASS

Verified through: CALL48_MATCH

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## MOCK49_UNKNOWN — Unknown key response

Input / fixture:

```text
<students><student><id>999</id></student></students>
```

Expected checks: {"observe": true}

Recorded: LIVE_CALLER / PASS

Verified through: CALL48_NOMATCH

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## MOCK49_EMPTY — Empty request response

Input / fixture:

```text
<students/>
```

Expected checks: {"observe": true}

Recorded: LIVE_CALLER / PASS

Verified through: CALL48_EMPTY

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Start Message

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [803 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${in.body} | Enter an expression or a constant to form new message body | When applicable |

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/MockLookup.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

Saved design messages: []

# TRN 050 - Send Local Message

Technical ID: IF_TRN_050_Send_Local_Message. Version 3.0.0.

Default Configure → ScenarioId=CALL50_MATCH. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| CALL50_MATCH | Matching lookup id | base | LIVE: PASS |
| CALL50_UTF8 | Unicode request preserved | base | LIVE: PASS |
| CALL50_NOMATCH | No matching lookup key | base | LIVE: PASS |
| CALL50_MULTI | Multiple request records | base | LIVE: PASS |

## CALL50_MATCH — Matching lookup id

Input / fixture:

```text
<students><student><id>1</id><name>Anu</name></student></students>
```

Expected checks: {"contains": ["Anu"], "receiverCall": true}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_ScenarioId": "CALL50_MATCH", "TRN_TestResult": "PASS", "TRN_OutputPreview": "<students><student><id>1</id><name>Anu</name></student></students>", "TRN_OutputSha256": "QAPpA1n0DB/Lyex8Mh5zN8DEPvPObC8QEe+J7axMQ90=", "TRN_Expected": "COMPLETED"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## CALL50_UTF8 — Unicode request preserved

Input / fixture:

```text
<students><student><id>1</id><name>సాయి</name></student></students>
```

Expected checks: {"contains": ["సాయి"], "receiverCall": true}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_OutputPreview": "<students><student><id>1</id><name>సాయి</name></student></students>", "TRN_TestResult": "PASS", "TRN_OutputSha256": "BMCU/cV+Efk5Dz6hlkCk7jJME+f6nirJAODuxL2tCjg=", "TRN_ScenarioId": "CALL50_UTF8", "TRN_Expected": "COMPLETED"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## CALL50_NOMATCH — No matching lookup key

Input / fixture:

```text
<students><student><id>999</id><name>Anu</name></student></students>
```

Expected checks: {"receiverCall": true, "observe": true}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_OutputPreview": "<students><student><id>999</id><name>Anu</name></student></students>", "TRN_OutputSha256": "b+Zt++wywu7QFsaAJxO8jkffXAipBL5/xMj6x9vB1do=", "TRN_Expected": "COMPLETED", "TRN_TestResult": "OBSERVED", "TRN_ScenarioId": "CALL50_NOMATCH"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## CALL50_MULTI — Multiple request records

Input / fixture:

```text
<students><student><id>1</id><name>Anu</name></student><student><id>1</id><name>Ravi</name></student></students>
```

Expected checks: {"contains": ["Anu", "Ravi"], "receiverCall": true}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_Expected": "COMPLETED", "TRN_ScenarioId": "CALL50_MULTI", "TRN_OutputPreview": "<students><student><id>1</id><name>Anu</name></student><student><id>1</id><name>Ravi</name></student></students>", "TRN_TestResult": "PASS", "TRN_OutputSha256": "uhfXIqub40YiazlbKCOk9NKvxwaFPPQaKhD6S9ZQ/qc="}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Send

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [839 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: []

# TRN 051 - Content Enricher

Technical ID: IF_TRN_051_Content_Enricher. Version 3.0.0.

Default Configure → ScenarioId=CALL51_MATCH. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| CALL51_MATCH | Matching lookup id | base | LIVE: PASS |
| CALL51_UTF8 | Unicode request preserved | base | LIVE: PASS |
| CALL51_NOMATCH | No matching lookup key | base | LIVE: PASS |
| CALL51_MULTI | Multiple request records | base | LIVE: PASS |

## CALL51_MATCH — Matching lookup id

Input / fixture:

```text
<students><student><id>1</id><name>Anu</name></student></students>
```

Expected checks: {"contains": ["Anu", "Sales"], "receiverCall": true}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_Expected": "COMPLETED", "TRN_TestResult": "PASS", "TRN_ScenarioId": "CALL51_MATCH", "TRN_OutputPreview": "<students><student><id>1</id><department><id>1</id><name>Sales</name></department><name>Anu</name></student></students>", "TRN_OutputSha256": "a14T6EsA9ZGq8UoKDxmJ4V/9Iod0/g7VmfMGV/nCkUQ="}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## CALL51_UTF8 — Unicode request preserved

Input / fixture:

```text
<students><student><id>1</id><name>సాయి</name></student></students>
```

Expected checks: {"contains": ["సాయి", "Sales"], "receiverCall": true}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_OutputPreview": "<students><student><id>1</id><department><id>1</id><name>Sales</name></department><name>సాయి</name></student></students>", "TRN_TestResult": "PASS", "TRN_Expected": "COMPLETED", "TRN_ScenarioId": "CALL51_UTF8", "TRN_OutputSha256": "Qpc9qBWvX99ynOkPBNXjSR9v5KuVn+73psdkMYnEXjo="}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## CALL51_NOMATCH — No matching lookup key

Input / fixture:

```text
<students><student><id>999</id><name>Anu</name></student></students>
```

Expected checks: {"receiverCall": true, "observe": true}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_OutputPreview": "<students><student><id>999</id><name>Anu</name></student></students>", "TRN_ScenarioId": "CALL51_NOMATCH", "TRN_TestResult": "OBSERVED", "TRN_Expected": "COMPLETED", "TRN_OutputSha256": "b+Zt++wywu7QFsaAJxO8jkffXAipBL5/xMj6x9vB1do="}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## CALL51_MULTI — Multiple request records

Input / fixture:

```text
<students><student><id>1</id><name>Anu</name></student><student><id>1</id><name>Ravi</name></student></students>
```

Expected checks: {"contains": ["Anu", "Ravi", "Sales"], "receiverCall": true}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_Expected": "COMPLETED", "TRN_ScenarioId": "CALL51_MULTI", "TRN_TestResult": "PASS", "TRN_OutputSha256": "skQEs6CjjkoZDtDV5JgLEnbFgzxFr4hBDl0peMMaRRI="}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Content Enricher

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Aggregation Algorithm | xmlLookupAggregation | Combine, Enrich | When applicable |
| Processing | Path to Node | /students/student | Path to the reference node in the original message. | Conditional |
| Processing | Key Element | id | Key element in the original message. | Conditional |
| Processing | Path to Node | /lookup/department | Path to the reference node in the lookup message. | Conditional |
| Processing | Key Element | id | Key element in the lookup message. | Conditional |

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [839 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: []

# TRN 053 - Aggregator

Technical ID: IF_TRN_053_Aggregator. Version 3.0.0.

ProcessDirect receiver: deploy before its caller; output remains usable by the caller.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| AGG53_ORDERED | Correlate two independently delivered messages | base | LIVE_PROTOCOL: PASS |
| AGG53_TIMEOUT | Timeout completes an incomplete group | base | LIVE_PROTOCOL: PASS |

## AGG53_ORDERED — Correlate two independently delivered messages

Input / fixture:

```text
<order><batch>TRN053</batch><sequence>1</sequence><last>false</last></order>
```

Expected checks: {"contains": ["1", "2"]}

Recorded: LIVE_PROTOCOL / PASS

Verified through: ['TRIGGER57_ORDERED', 'TRIGGER57_REVERSED', 'TRIGGER57_VERIFY', 'TRIGGER57_TIMEOUT', 'TRIGGER57_VERIFY_TIMEOUT']

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## AGG53_TIMEOUT — Timeout completes an incomplete group

Input / fixture:

```text
<order><batch>TRN053_TIMEOUT</batch><sequence>1</sequence><last>false</last></order>
```

Expected checks: {"observe": true}

Recorded: LIVE_PROTOCOL / PASS

Verified through: ['TRIGGER57_ORDERED', 'TRIGGER57_REVERSED', 'TRIGGER57_VERIFY', 'TRIGGER57_TIMEOUT', 'TRIGGER57_VERIFY_TIMEOUT']

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### Write

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Data Store Name | TRN053_LAB_RESULTS | Enter a data store name (maximum 40 characters). The name should begin with an alphabetic character and use characters (aA-zZ, 0-9, - _ . ~ ) without any whitespace. You can also enter ${header.header … [298 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | Visibility | global | Integration Flow, Global | When applicable |
| Processing | Entry ID | ${header.TRN_BatchId} | Entry id with which content will be persisted, you can use ${header.headername}, ${property.propertyname} or ${xpath.node} to read entry id details from incoming message. A generated GUID would be use … [223 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | Retention Threshold for Alerting (in d) | 1 | Enter a time period (in days). If messages are not fetched within this period, an alert is raised. The default value is 2. | When applicable |
| Processing | Expiration Period (in d) | 2 | Enter a time period (in days). The stored message is automatically deleted from the data store after this period. The default value is 30 days. | When applicable |
| Processing | Encrypt Stored Message | true | Select this option to encrypt the message in data store | When applicable |
| Processing | Overwrite Existing Message | true | Select this option to overwrite the message in data store | When applicable |
| Processing | Include Message Headers | false | Select this option to also store the message's headers | When applicable |

### End Event

No additional component processing fields; inspect General and the surrounding message flow.

### Start Message

No additional component processing fields; inspect General and the surrounding message flow.

### Aggregator

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Correlation | Correlation Expression (XPath) | /order/batch | Enter an XPath expression that identifies an element based on which the incoming messages should be correlated | When applicable |
| Aggregation Strategy | Incoming Format | XML_SAME_FORMAT | XML (Same Format) | When applicable |
| Aggregation Strategy | Aggregation Algorithm | sap-sequenced-id-list | Combine, Combine in Sequence | When applicable |
| Aggregation Strategy | Message Sequence Expression (XPath) | /order/sequence | Enter an XPath expression which is used to determine the sequence number | Conditional |
| Aggregation Strategy | Last Message Condition (XPath) | /order/last = 'true' | Enter an XPath condition which is used to determine the last message of the aggregate | When applicable |
| Aggregation Strategy | Completion Timeout (in min) | 1 | Enter completion timeout in minutes | When applicable |
| Aggregation Strategy | Timeout Measured From | first-message | Arrival Time of First Message for Aggregation, Arrival Time of Last Message for Aggregation | When applicable |
| Aggregation Strategy | Completion Size | 2 | Enter maximum message number before automatic completion | When applicable |
| Aggregation Strategy | Data Store Name | TRN053_AGGREGATOR_V3 | Enter the name of the transient data store where the aggregated messages are stored during processing. The name should begin with an alphabetic character and use characters (aA-zZ, 0-9, - _ . ~ ) with … [284 characters; full value in scenario/evidence JSON] | When applicable |

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [773 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [803 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${in.body} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: [{"severity": "Warning", "message": "Content Modifier may not pass XML message to Aggregator. Aggregator supports XML input only.", "elementId": "CallActivity_11", "elementType": "CallActivity", "location": "Correlate Orders", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 053 - Aggregator.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}]

# TRN 054 - EDI Extractor

Technical ID: IF_TRN_054_EDI_Extractor. Version 3.0.0.

Default Configure → ScenarioId=EDI54_ORDER. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| EDI54_ORDER | Extract EDIFACT envelope and message headers | base | SIMULATION: PASS |
| EDI54_PARTNER | Different partner identifiers | base | SIMULATION: PASS |
| EDI54_CONTROL | Interchange control number | base | SIMULATION: PASS |
| EDI54_BAD | Malformed EDI input | base | SIMULATION: EXPECTED_REJECTION |

## EDI54_ORDER — Extract EDIFACT envelope and message headers

Input / fixture:

```text
UNA:+.? 'UNB+UNOC:3+SENDER:14+RECEIVER:14+261117:1200+1'UNH+1+ORDERS:D:96A:UN'BGM+220+PO123+9'DTM+137:20261117:102'UNT+4+1'UNZ+1+1'
```

Expected checks: {"headers": {"EDI_Document_Standard": "UN-EDIFACT", "EDI_Message_Type": "ORDERS", "EDI_Message_Release": "96A"}, "exact": "UNA:+.? 'UNB+UNOC:3+SENDER:14+RECEIVER:14+261117:1200+1'UNH+1+ORDERS:D:96A:UN'BGM+220+PO123+9'DTM+137:20261117:102'UNT+4+1'UNZ+1+1'"}

Recorded: SIMULATION / PASS

Native output:

```text
UNA:+.? 'UNB+UNOC:3+SENDER:14+RECEIVER:14+261117:1200+1'UNH+1+ORDERS:D:96A:UN'BGM+220+PO123+9'DTM+137:20261117:102'UNT+4+1'UNZ+1+1'
```

Java type: java.lang.String; bytes: 131

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## EDI54_PARTNER — Different partner identifiers

Input / fixture:

```text
UNA:+.? 'UNB+UNOC:3+TRAININGA:14+TRAININGB:14+261117:1200+1'UNH+1+ORDERS:D:96A:UN'BGM+220+PO123+9'DTM+137:20261117:102'UNT+4+1'UNZ+1+1'
```

Expected checks: {"headers": {"EDI_Sender_ID": "TRAININGA", "EDI_Receiver_ID": "TRAININGB"}}

Recorded: SIMULATION / PASS

Native output:

```text
UNA:+.? 'UNB+UNOC:3+TRAININGA:14+TRAININGB:14+261117:1200+1'UNH+1+ORDERS:D:96A:UN'BGM+220+PO123+9'DTM+137:20261117:102'UNT+4+1'UNZ+1+1'
```

Java type: java.lang.String; bytes: 135

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## EDI54_CONTROL — Interchange control number

Input / fixture:

```text
UNA:+.? 'UNB+UNOC:3+SENDER:14+RECEIVER:14+261117:1200+123'UNH+1+ORDERS:D:96A:UN'BGM+220+PO123+9'DTM+137:20261117:102'UNT+4+1'UNZ+1+123'
```

Expected checks: {"headers": {"EDI_Interchange_control_number": "123"}}

Recorded: SIMULATION / PASS

Native output:

```text
UNA:+.? 'UNB+UNOC:3+SENDER:14+RECEIVER:14+261117:1200+123'UNH+1+ORDERS:D:96A:UN'BGM+220+PO123+9'DTM+137:20261117:102'UNT+4+1'UNZ+1+123'
```

Java type: java.lang.String; bytes: 135

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## EDI54_BAD — Malformed EDI input

Input / fixture:

```text
NOT AN EDI MESSAGE
```

Expected checks: {"negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
Invalid Payload: EDI EXTRACTION cannot process the payload. on the exchange: Exchange[208BE7709037F5B-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### EDI Extractor

No additional component processing fields; inspect General and the surrounding message flow.

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/LoadLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [817 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: [{"severity": "Warning", "message": "Script may not pass Text message to EDI Extractor. EDI Extractor supports Text input only.", "elementId": "CallActivity_100", "elementType": "CallActivity", "location": "base - EDI Extractor", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 054 - EDI Extractor.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}]

# TRN 055 - EDI Splitter

Technical ID: IF_TRN_055_EDI_Splitter. Version 3.0.0.

Default Configure → ScenarioId=EDI55_TWO. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| EDI55_TWO | Two messages split and gathered | base | SIMULATION: PASS |
| EDI55_ONE | Single message | base | SIMULATION: PASS |
| EDI55_PARTNER | Partner-specific envelope preserved | base | SIMULATION: PASS |
| EDI55_BAD | Invalid EDI input | base | SIMULATION: EXPECTED_REJECTION |

## EDI55_TWO — Two messages split and gathered

Input / fixture:

```text
UNA:+.? 'UNB+UNOC:3+SENDER:14+RECEIVER:14+261117:1200+2'UNH+1+ORDERS:D:96A:UN'BGM+220+PO123+9'DTM+137:20261117:102'UNT+4+1'UNH+2+ORDERS:D:96A:UN'BGM+220+PO124+9'DTM+137:20261117:102'UNT+4+2'UNZ+2+2'
```

Expected checks: {"contains": ["PO123", "PO124"]}

Recorded: SIMULATION / PASS

Native output:

```text
UNA:+.? 'UNB+UNOC:3+SENDER:14+RECEIVER:14+261117:1200+2'UNH+1+ORDERS:D:96A:UN'BGM+220+PO123+9'DTM+137:20261117:102'UNT+4+1'UNZ+1+2'UNA:+.? 'UNB+UNOC:3+SENDER:14+RECEIVER:14+261117:1200+2'UNH+2+ORDERS:D:96A:UN'BGM+220+PO124+9'DTM+137:20261117:102'UNT+4+2'UNZ+1+2'
```

Java type: [B; bytes: 262

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## EDI55_ONE — Single message

Input / fixture:

```text
UNA:+.? 'UNB+UNOC:3+SENDER:14+RECEIVER:14+261117:1200+1'UNH+1+ORDERS:D:96A:UN'BGM+220+PO123+9'DTM+137:20261117:102'UNT+4+1'UNZ+1+1'
```

Expected checks: {"contains": ["PO123"]}

Recorded: SIMULATION / PASS

Native output:

```text
UNA:+.? 'UNB+UNOC:3+SENDER:14+RECEIVER:14+261117:1200+1'UNH+1+ORDERS:D:96A:UN'BGM+220+PO123+9'DTM+137:20261117:102'UNT+4+1'UNZ+1+1'
```

Java type: [B; bytes: 131

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## EDI55_PARTNER — Partner-specific envelope preserved

Input / fixture:

```text
UNA:+.? 'UNB+UNOC:3+TRAININGA:14+RECEIVER:14+261117:1200+2'UNH+1+ORDERS:D:96A:UN'BGM+220+PO123+9'DTM+137:20261117:102'UNT+4+1'UNH+2+ORDERS:D:96A:UN'BGM+220+PO124+9'DTM+137:20261117:102'UNT+4+2'UNZ+2+2'
```

Expected checks: {"contains": ["TRAININGA", "PO123", "PO124"]}

Recorded: SIMULATION / PASS

Native output:

```text
UNA:+.? 'UNB+UNOC:3+TRAININGA:14+RECEIVER:14+261117:1200+2'UNH+1+ORDERS:D:96A:UN'BGM+220+PO123+9'DTM+137:20261117:102'UNT+4+1'UNZ+1+2'UNA:+.? 'UNB+UNOC:3+TRAININGA:14+RECEIVER:14+261117:1200+2'UNH+2+ORDERS:D:96A:UN'BGM+220+PO124+9'DTM+137:20261117:102'UNT+4+2'UNZ+1+2'
```

Java type: [B; bytes: 268

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## EDI55_BAD — Invalid EDI input

Input / fixture:

```text
NOT AN EDI MESSAGE
```

Expected checks: {"negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
Invalid Payload: EDI SPLITTER cannot process the payload. on the exchange: Exchange[191D4D14A448EF8-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Gather

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Aggregation Strategy | Incoming Format | PlainText | Any, XML (Same Format), XML (Different Format), Plain Text | When applicable |
| Aggregation Strategy | Aggregation Algorithm | concatenate-strategy | Combine, Combine at XPath, Concatenate, Zip, Tar | When applicable |
| Aggregation Strategy | Combine from source (XPath) |  |  | Conditional |
| Aggregation Strategy | Combine at target (XPath) |  |  | Conditional |
| Aggregation Strategy | File Name |  | Configure a simple expression to define unique file names within the archive. If not specified, the header CamelFileName is evaluated. In case neither an expression is defined nor CamelFileName is ava … [255 characters; full value in scenario/evidence JSON] | Conditional |

### EDI Splitter

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Parallel Processing | false | Allows simultaneous processing of split messages within a specific time period. | When applicable |
| Processing | Timeout (in sec) |  | Splitter operation processes the split messages within the specified time. If the processing fails during the specified time, then the next split messages in the sequence are processed. | Conditional |
| EDIFACT | Source Encoding | UTF-8 | ISO-8859-1, UTF-8, Dynamic | When applicable |
| EDIFACT | Validate Message | false | Validates the incoming messages. | When applicable |
| EDIFACT | Validate | envelopAndMessage | Envelop And Message, Envelop | Conditional |
| EDIFACT | Decimal Character | dot | Dot(.), Dynamic, From Incoming Payload | Conditional |
| EDIFACT | Transaction Mode | interchange | Interchange, Message | Conditional |
| EDIFACT | EDI Schema Definition | IntegrationProject | Integration Flow, Header | Conditional |
| EDIFACT | Schemas | [] | Validates the XSD schema for conversion. | Conditional |
| EDIFACT | Schema Name |  |  | When applicable |
| EDIFACT | Header Name |  | Header name of the XSD schema. | Conditional |
| EDIFACT | Process Invalid Messages | false | Routes invalid messages with the header, EDI_MESSAGE_STATUS=failure. | Conditional |
| EDIFACT | Create Acknowledgement | notRequired | Required, Not Required, Check EDI Envelop | When applicable |
| EDIFACT | Customize Envelope | fromIncomingPayload | Dynamic, From Incoming Payload, Overwrite with Exchange Headers, Remove Optional Trailing Field | Conditional |
| EDIFACT | Interchange Number | useFromEDIMessage | Use From EDI Message, Number Range | Conditional |
| EDIFACT | Unique Interchange Number | notRequired | Not Required, Required, Dynamic | Conditional |
| EDIFACT | Number Range |  | Name of the Number Range that is deployed on the cluster. | Conditional |
| EDIFACT | CONTRL Message Version | defaultVersion | Version D, Release 3, Use From EDI Message | Conditional |
| EDIFACT | Include UNA Segment | false | Includes the UNA segment in the EDI Functional Acknowledgement. | Conditional |
| X12 | Source Encoding | UTF-8 | ISO-8859-1, UTF-8, Dynamic | When applicable |
| X12 | Validate Message | none | No Validation, Standard Validation | When applicable |
| X12 | Transaction Mode | interchange | Interchange, Message | Conditional |
| X12 | EDI Schema Definition | IntegrationProject | Integration Flow, Header | Conditional |
| X12 | Schemas | [] | Validates the XSD schema for conversion. | Conditional |
| X12 | Schema Name |  |  | When applicable |
| X12 | Header Name |  | Header name of the XSD schema. | Conditional |
| X12 | Process Invalid Messages | false | Routes invalid messages with the header, EDI_MESSAGE_STATUS=failure. | Conditional |
| X12 | Create Acknowledgement | notRequired | Required, Not Required, Check EDI Envelop | When applicable |
| X12 | Exclude AK3 and AK4 | false | Excludes the AK3 and AK4 segments from the message. | Conditional |
| X12 | Customize Envelope | fromIncomingPayload | Dynamic, From Incoming Payload, Overwrite with Exchange Headers | Conditional |
| X12 | Interchange Number | useFromEDIMessage | Use From EDI Message, Number Range, Dynamic | Conditional |
| X12 | Unique Interchange Number | notRequired | Not Required, Required, Dynamic | Conditional |
| X12 | Number Range |  | Name of the number range for interchange number that is deployed on the cluster. You can also enter ${header.<header-name>} or ${property.<property-name>} to read the value dynamically from a header o … [213 characters; full value in scenario/evidence JSON] | Conditional |
| X12 | Group Number | predefined | Use From EDI Message, Number Range, Dynamic, Predefined (1) | Conditional |
| X12 | Unique Group Number | notRequired | Not Required, Required, Dynamic | Conditional |
| X12 | Group Number Range |  | Name of the number range for functional group number that is deployed on the cluster.You can also enter ${header.<header-name>} or ${property.<property-name>} to read the value dynamically from a head … [217 characters; full value in scenario/evidence JSON] | Conditional |
| X12 | Transaction Set Number | predefined | Predefined (0001), Number Range, Dynamic | Conditional |
| X12 | Unique Transaction Set Number | notRequired | Not Required, Required, Dynamic | Conditional |
| X12 | Transaction Set Number Range |  | Name of the number range for transaction set number that is deployed on the cluster.You can also enter ${header.<header-name>} or ${property.<property-name>} to read the value dynamically from a heade … [216 characters; full value in scenario/evidence JSON] | Conditional |
| TRADACOMS | Source Encoding | ISO-8859-1 | ISO-8859-1, UTF-8, Dynamic | When applicable |
| TRADACOMS | Split Preference | NoSplit | No Split, Without Validation, Dynamic | When applicable |
| TRADACOMS | EDI Schema Definition | IntegrationProject | Integration Flow, Header, Dynamic | Conditional |
| TRADACOMS | Header Name |  | Enter header name. | Conditional |
| TRADACOMS | Schemas | [] | Select the valid schemas for conversion. | Conditional |
| TRADACOMS | Schema Name |  |  | When applicable |
| VDA | Source Encoding | ISO-8859-1 | ISO-8859-1, UTF-8, Dynamic | When applicable |
| VDA | Split Preference | noSplit | No Split, Without Validation, Dynamic | When applicable |
| VDA | EDI Schema Definition | integrationProject | Integration Flow, Header, Dynamic | Conditional |
| VDA | Header Name |  | Enter header name. | Conditional |
| VDA | Schemas | [] | Select the valid schemas for conversion. | Conditional |
| VDA | Schema Name |  |  | When applicable |

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [813 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: [{"severity": "Warning", "message": "Script may not pass non-XML message to EDISplitter. EDISplitter supports non-XML input only.", "elementId": "CallActivity_100", "elementType": "CallActivity", "location": "base - EDI Splitter", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 055 - EDI Splitter.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}]

# TRN 056 - EDI XML Roundtrip

Technical ID: IF_TRN_056_EDI_XML_Roundtrip. Version 3.0.0.

Default Configure → ScenarioId=EDI56_NO_SCHEMA. Save and deploy once.

A licensed UN/EDIFACT ORDERS D96A Integration Advisor runtime XSD is required. SAP currently reports: You do not have valid license for the type system.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| EDI56_NO_SCHEMA | Prerequisite diagnostic: missing licensed runtime XSD is rejected; this does not test successful EDI conversion | base | LIVE: PASS |
| EDI56_ORDER | EDIFACT order to XML and back | base | PREREQUISITE: BLOCKED |
| EDI56_OTHER | Changed purchase-order reference | base | PREREQUISITE: BLOCKED |
| EDI56_BAD | Malformed input is rejected before any EDI conversion can complete | base | PREREQUISITE: BLOCKED |

## EDI56_NO_SCHEMA — Prerequisite diagnostic: missing licensed runtime XSD is rejected; this does not test successful EDI conversion

Input / fixture:

```text
UNA:+.? 'UNB+UNOC:3+SENDER:14+RECEIVER:14+261117:1200+1'UNH+1+ORDERS:D:96A:UN'BGM+220+PO123+9'DTM+137:20261117:102'UNT+4+1'UNZ+1+1'
```

Expected checks: {"negative": true, "prerequisiteCheck": true}

Recorded: LIVE / PASS; runtime FAILED

```text
b'com.sap.it.rt.edi.exception.EDIToXMLConverterException: Error while processing the edifact file: Resource during resource lookup: /xsd/UN-EDIFACT_ORDERS_D96A.xsd exception: , cause: java.io.FileNotFoundException: Cannot find resource: /xsd/UN-EDIFACT_ORDERS_D96A.xsd for URI: /xsd/UN-EDIFACT_ORDERS_D96A.xsd'
```

Live checks: {"TRN_Expected": "FAILED", "TRN_ScenarioId": "EDI56_NO_SCHEMA"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## EDI56_ORDER — EDIFACT order to XML and back

Input / fixture:

```text
UNA:+.? 'UNB+UNOC:3+SENDER:14+RECEIVER:14+261117:1200+1'UNH+1+ORDERS:D:96A:UN'BGM+220+PO123+9'DTM+137:20261117:102'UNT+4+1'UNZ+1+1'
```

Expected checks: {"contains": ["PO123"], "blocked": "A licensed UN/EDIFACT ORDERS D96A Integration Advisor runtime XSD is required. SAP currently reports: You do not have valid license for the type system."}

Recorded: PREREQUISITE / BLOCKED

A licensed UN/EDIFACT ORDERS D96A Integration Advisor runtime XSD is required. SAP currently reports: You do not have valid license for the type system.

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## EDI56_OTHER — Changed purchase-order reference

Input / fixture:

```text
UNA:+.? 'UNB+UNOC:3+SENDER:14+RECEIVER:14+261117:1200+1'UNH+1+ORDERS:D:96A:UN'BGM+220+PO999+9'DTM+137:20261117:102'UNT+4+1'UNZ+1+1'
```

Expected checks: {"contains": ["PO999"], "blocked": "A licensed UN/EDIFACT ORDERS D96A Integration Advisor runtime XSD is required. SAP currently reports: You do not have valid license for the type system."}

Recorded: PREREQUISITE / BLOCKED

A licensed UN/EDIFACT ORDERS D96A Integration Advisor runtime XSD is required. SAP currently reports: You do not have valid license for the type system.

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## EDI56_BAD — Malformed input is rejected before any EDI conversion can complete

Input / fixture:

```text
NOT AN EDI MESSAGE
```

Expected checks: {"negative": true, "blocked": "A licensed UN/EDIFACT ORDERS D96A Integration Advisor runtime XSD is required. SAP currently reports: You do not have valid license for the type system."}

Recorded: PREREQUISITE / BLOCKED

A licensed UN/EDIFACT ORDERS D96A Integration Advisor runtime XSD is required. SAP currently reports: You do not have valid license for the type system.

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### XML to EDI Converter

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| EDIFACT | Source Encoding | ISO-8859-1 | ISO-8859-1, UTF-8, Dynamic | When applicable |
| EDIFACT | Target Encoding | ISO-8859-1 | ISO-8859-1, ISO-8859-2, ISO-8859-3, ISO-8859-4, ISO-8859-5, ISO-8859-6, ISO-8859-7, ISO-8859-8, ISO-8859-9, UTF-8, Dynamic, From incoming payload | When applicable |
| EDIFACT | EDI Schema Definition | Header | Integration Flow, Header | When applicable |
| EDIFACT | Schemas | [] | Select the valid schemas for conversion. | Conditional |
| EDIFACT | Schema Name |  |  | When applicable |
| EDIFACT | Header Name | TRN_EdiSchema | Enter header name. | Conditional |
| EDIFACT | Empty Segment | Exclude | Exclude, Include, Dynamic | When applicable |
| EDIFACT | Use Custom Separators | false | Select if you want to use custom separators in converted message. | When applicable |
| EDIFACT | Segment Terminator | #x27 | Apostrophe('), Bell, Backspace, Tab, Line Feed, Vertical Tab, Form Feed, Carriage Return, Escape, Whitespace | Conditional |
| EDIFACT | Composite Separator | #x3a | Colon(:), Plus(+), Semicolon(;), Comma(,), Pipe(\|), Dot(.) | Conditional |
| EDIFACT | Data Element Separator | #x2b | Colon(:), Plus(+), Semicolon(;), Comma(,), Pipe(\|), Dot(.) | Conditional |
| EDIFACT | Escape Character | #x3f | Question Mark(?), Colon(:), Plus(+), Semicolon(;), Comma(,), Pipe(\|), Dot(.) | Conditional |
| EDIFACT | Decimal Character | #x2e | Comma(,), Dot(.), Dynamic | Conditional |
| X12 | Source Encoding | ISO-8859-1 | ISO-8859-1, UTF-8, Dynamic | When applicable |
| X12 | EDI Schema Definition | IntegrationProject | Integration Flow, Header | When applicable |
| X12 | Schemas | [] | Select the valid schemas for conversion. | Conditional |
| X12 | Schema Name |  |  | When applicable |
| X12 | Header Name |  | Enter header name. | Conditional |
| X12 | Empty Segment | Exclude | Exclude, Include, Dynamic | When applicable |
| X12 | Use Custom Separators | false | Select if you want to use custom separators in converted message. | When applicable |
| X12 | Segment Terminator | #x7e | Tilde(~), Bell, Backspace, Tab, Line Feed, Vertical Tab, Form Feed, Carriage Return, Escape, Whitespace | Conditional |
| X12 | Composite Separator | #x3a | Backslash(\), Colon(:), Plus(+), Semicolon(;), Comma(,), Pipe(\|), Dot(.) | Conditional |
| X12 | Data Element Separator | #x2a | Star(*), Colon(:), Plus(+), Semicolon(;), Comma(,), Pipe(\|), Dot(.) | Conditional |
| X12 | Repetition Separator | #x5e | Caret(^), Colon(:), Plus(+), Semicolon(;), Comma(,), Pipe(\|), Dot(.) | Conditional |
| TRADACOMS | Source Encoding | ISO-8859-1 | ISO-8859-1, UTF-8, Dynamic | When applicable |
| TRADACOMS | Conversion Preference | No | No, Yes, Dynamic | When applicable |
| TRADACOMS | EDI Schema Definition | IntegrationProject | Integration Flow, Header, Dynamic | Conditional |
| TRADACOMS | Header Name |  | Enter header name. | Conditional |
| TRADACOMS | Schemas | [] | Select the valid schemas for conversion. | Conditional |
| TRADACOMS | Schema Name |  |  | When applicable |
| TRADACOMS | Empty Segment | Exclude | Exclude, Include, Dynamic | Conditional |
| VDA | Source Encoding | ISO-8859-1 | ISO-8859-1, UTF-8, Dynamic | When applicable |
| VDA | Target Encoding | ISO-8859-1 | ISO-8859-1, ISO-8859-2, ISO-8859-3, ISO-8859-4, ISO-8859-5, ISO-8859-6, ISO-8859-7, ISO-8859-8, ISO-8859-9, UTF-8, Dynamic, From incoming payload | When applicable |
| VDA | Conversion Preference | no | No, Yes, Dynamic | When applicable |
| VDA | EDI Schema Definition | IntegrationProject | Integration Flow, Header, Dynamic | Conditional |
| VDA | Header Name |  | Enter header name. | Conditional |
| VDA | Schemas | [] | Select the valid schemas for conversion. | Conditional |
| VDA | Schema Name |  |  | When applicable |
| VDA | Segment Terminator | none | None, Line Feed, Carriage Return, Dynamic | Conditional |

### EDI to XML Converter

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| EDIFACT | Source Encoding | ISO-8859-1 | ISO-8859-1, UTF-8, Dynamic | When applicable |
| EDIFACT | Target Encoding | ISO-8859-1 | ISO-8859-1, UTF-8, Dynamic | When applicable |
| EDIFACT | Target Decimal Character | fromIncomingPayload | Dot(.), Dynamic, From Incoming Payload | When applicable |
| EDIFACT | EDI Schema Definition | Header | Integration Flow, Header | When applicable |
| EDIFACT | Schemas | [] | Select the valid schemas for conversion. | Conditional |
| EDIFACT | Schema Name |  |  | When applicable |
| EDIFACT | Header Name | TRN_EdiSchema | Enter header name. | Conditional |
| EDIFACT | Empty Segment | Exclude | Exclude, Include, Dynamic | When applicable |
| EDIFACT | Exclude Interchange and Group envelopes | true | The interchange and functional group envelopes of an EDI file are excluded before<br>                        conversion. | When applicable |
| EDIFACT | Target Root Element | interchange | Interchange, Message Specification from Incoming Payload, Dynamic | Conditional |
| X12 | Source Encoding | ISO-8859-1 | ISO-8859-1, UTF-8, Dynamic | When applicable |
| X12 | EDI Schema Definition | IntegrationProject | Integration Flow, Header | When applicable |
| X12 | Schemas | [] | Select the valid schemas for conversion. | Conditional |
| X12 | Schema Name |  |  | When applicable |
| X12 | Header Name |  | Enter header name. | Conditional |
| X12 | Empty Segment | Exclude | Exclude, Include, Dynamic | When applicable |
| X12 | Exclude Interchange and Group envelopes | true | The interchange and functional group envelopes of an EDI file are excluded before<br>                        conversion. | When applicable |
| X12 | Target Root Element | interchange | Interchange, Message Specification from Incoming Payload, Dynamic | Conditional |
| TRADACOMS | Source Encoding | ISO-8859-1 | ISO-8859-1, UTF-8, Dynamic | When applicable |
| TRADACOMS | Conversion Preference | No | No, Yes, Dynamic | When applicable |
| TRADACOMS | EDI Schema Definition | IntegrationProject | Integration Flow, Header, Dynamic | Conditional |
| TRADACOMS | Header Name |  | Enter header name. | Conditional |
| TRADACOMS | Schemas | [] | Select the valid schemas for conversion. | Conditional |
| TRADACOMS | Schema Name |  |  | When applicable |
| TRADACOMS | Empty Segment | Exclude | Exclude, Include, Dynamic | Conditional |
| VDA | Source Encoding | ISO-8859-1 | ISO-8859-1, UTF-8, Dynamic | When applicable |
| VDA | Target Encoding | ISO-8859-1 | ISO-8859-1, UTF-8, Dynamic | When applicable |
| VDA | Conversion Preference | no | No, Yes, Dynamic | When applicable |
| VDA | EDI Schema Definition | IntegrationProject | Integration Flow, Header, Dynamic | Conditional |
| VDA | Header Name |  | Enter header name. | Conditional |
| VDA | Schemas | [] | Select the valid schemas for conversion. | Conditional |
| VDA | Schema Name |  |  | When applicable |

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [789 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [845 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: [{"severity": "Warning", "message": "Script may not pass non-XML message to EDI to XML Converter. EDI to XML Converter supports non-XML input only.", "elementId": "CallActivity_100", "elementType": "CallActivity", "location": "base - EDI to XML Converter", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 056 - EDI XML Roundtrip.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}]

# TRN 057 - Aggregator Trigger

Technical ID: IF_TRN_057_Aggregator_Trigger. Version 3.0.0.

Default Configure → ScenarioId=TRIGGER57_ORDERED. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| TRIGGER57_ORDERED | Send sequences one then two | base | LIVE: PASS |
| TRIGGER57_REVERSED | Out-of-order delivery with sequence two correctly marked as last | base | LIVE: PASS |
| TRIGGER57_VERIFY | Read/delete aggregated XML and assert ordered sequence values | verify | LIVE: PASS |
| TRIGGER57_TIMEOUT | Send an incomplete group; wait for configured timeout | single | LIVE: PASS |
| TRIGGER57_VERIFY_TIMEOUT | After timeout, assert the one-message result | verify | LIVE: PASS |
| TRIGGER57_BAD_LAST | Reject a last-message marker below an already accepted sequence | base | LIVE: PASS |

## TRIGGER57_ORDERED — Send sequences one then two

Input / fixture:

```text
<order><batch>TRN053</batch><sequence>1</sequence><last>false</last></order>
```

Properties: {"SecondOrder": "<order><batch>TRN053</batch><sequence>2</sequence><last>true</last></order>"}

Expected checks: {"observe": true}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_TestResult": "OBSERVED", "TRN_OutputPreview": "<order><batch>TRN053_cde8e188-a0ec-4288-a3af-12edd905a450</batch><sequence>2</sequence><last>true</last></order>", "TRN_OutputSha256": "ylTXThsH1pTJpJTTfvKR6BmIYMtzvQOs/kYfFf0yVLA=", "TRN_Expected": "COMPLETED", "TRN_ScenarioId": "TRIGGER57_ORDERED"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## TRIGGER57_REVERSED — Out-of-order delivery with sequence two correctly marked as last

Input / fixture:

```text
<order><batch>TRN053_REV</batch><sequence>2</sequence><last>true</last></order>
```

Properties: {"SecondOrder": "<order><batch>TRN053_REV</batch><sequence>1</sequence><last>false</last></order>"}

Expected checks: {"observe": true}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_OutputSha256": "0LJ6y6JjlXCxXM3CnWbpLWpqGn1H9eV7vAgUXgZQ6tE=", "TRN_TestResult": "OBSERVED", "TRN_OutputPreview": "<order><batch>TRN053_08257635-a8fe-4e45-9b34-b55262280c17_REV</batch><sequence>1</sequence><last>false</last></order>", "TRN_Expected": "COMPLETED", "TRN_ScenarioId": "TRIGGER57_REVERSED"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## TRIGGER57_VERIFY — Read/delete aggregated XML and assert ordered sequence values

Input / fixture:

```text
Verify latest aggregated message
```

Expected checks: {"xmlValues": {"sequence": ["1", "2"]}}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_ScenarioId": "TRIGGER57_VERIFY", "TRN_TestResult": "PASS", "TRN_OutputSha256": "gDfwOXdgtG37hFQRCc+tNbSa2zRQ7uC8RDgTbqEMoGE=", "TRN_Expected": "COMPLETED"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## TRIGGER57_TIMEOUT — Send an incomplete group; wait for configured timeout

Input / fixture:

```text
<order><batch>TRN053_TIMEOUT</batch><sequence>1</sequence><last>false</last></order>
```

Expected checks: {"observe": true}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_OutputSha256": "lJMlB60kjWUAHcMNQF0p74RlbNiPpkt4xWPoSqKz6Es=", "TRN_ScenarioId": "TRIGGER57_TIMEOUT", "TRN_OutputPreview": "<order><batch>TRN053_031cb5fe-125c-4744-a9da-1f74f3db76b4_TIMEOUT</batch><sequence>1</sequence><last>false</last></order>", "TRN_Expected": "COMPLETED", "TRN_TestResult": "OBSERVED"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## TRIGGER57_VERIFY_TIMEOUT — After timeout, assert the one-message result

Input / fixture:

```text
Verify incomplete group
```

Expected checks: {"xmlValues": {"sequence": ["1"]}}

Recorded: LIVE / PASS; runtime COMPLETED

Live checks: {"TRN_TestResult": "PASS", "TRN_Expected": "COMPLETED", "TRN_OutputSha256": "z2XiCslmUeIbybxfgjHvuWHM8aBlCTFVB2vF6jVaYas=", "TRN_ScenarioId": "TRIGGER57_VERIFY_TIMEOUT"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## TRIGGER57_BAD_LAST — Reject a last-message marker below an already accepted sequence

Input / fixture:

```text
<order><batch>TRN053_REV</batch><sequence>2</sequence><last>false</last></order>
```

Properties: {"SecondOrder": "<order><batch>TRN053_REV</batch><sequence>1</sequence><last>true</last></order>"}

Expected checks: {"negative": true}

Recorded: LIVE / PASS; runtime FAILED

```text
b'org.apache.camel.RuntimeCamelException: org.apache.camel.CamelExchangeException: Error occurred during aggregation. Exchange[0B4BE7C80BB0CE8-000000000000001E]. Caused by: [java.lang.IllegalStateException - Message 1 is marked as last message, but message 2 was already accepted.], cause: java.lang.IllegalStateException: Message 1 is marked as last message, but message 2 was already accepted.'
```

Live checks: {"TRN_ScenarioId": "TRIGGER57_BAD_LAST", "TRN_Expected": "FAILED"}

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### Router

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Throw Exception | false | Exception is thrown only if the sender channel is either IDoc or SOAP adapter, and the default route contains only Terminate End Event. | When applicable |
| Processing | conditionTable | [{"branchId": {"value": "SequenceFlow_1861", "defaultValue": "", "key": ""}, "defaultBranch": {"value": "false", "defaultValue": "", "key": ""}, "conditionValue … [1231 characters; full value in scenario/evidence JSON] |  | When applicable |
| Processing | Order |  |  | When applicable |
| Processing | Route Name |  |  | When applicable |
| Processing | Condition Expression |  |  | When applicable |
| Processing | Default Route |  |  | When applicable |

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Get

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Data Store Name | TRN053_LAB_RESULTS | Enter a data store name (maximum 40 characters). The name should begin with an alphabetic character and use characters (aA-zZ, 0-9, - _ . ~ ) without any whitespace. You can also enter ${header.header … [298 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | Visibility | global | Integration Flow, Global | When applicable |
| Processing | Entry ID | ${property.ExpectedBatchId} | Specify the corresponding Entry ID of the content to be fetched from data store. You can enter ${header.headername}, ${property.propertyname} or ${xpath.node} to read the Entry ID from the incoming me … [287 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | Delete On Completion | true | Select this option to delete messages from data store after successfully processing the messages | When applicable |
| Processing | Throw Exception on Missing Entry | true | You have the option to throw an exception in the message processing if the entry with the specified ID does not exist in the datastore. If you disable this option, the header SAP_DataStoreEntryFound i … [277 characters; full value in scenario/evidence JSON] | When applicable |

### Send

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.SecondOrder} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: []

# TRN 058 - Message Mapping

Technical ID: IF_TRN_058_Message_Mapping. Version 3.0.0.

Default Configure → ScenarioId=MM58_NAME. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| MM58_NAME | Graphical field mapping | base | SIMULATION: PASS |
| MM58_UTF8 | Unicode mapping | base | SIMULATION: PASS |
| MM58_EMPTY | Empty source field | base | SIMULATION: PASS |
| MM58_ESCAPE | Escaped XML data | base | SIMULATION: PASS |
| MM58_BAD | Malformed XML rejected | base | SIMULATION: EXPECTED_REJECTION |

## MM58_NAME — Graphical field mapping

Input / fixture:

```text
<student><name>Anu</name></student>
```

Expected checks: {"xmlRoot": "customer", "contains": ["Anu"]}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version="1.0" encoding="UTF-8"?>
<customer><fullName>Anu</fullName></customer>
```

Java type: [B; bytes: 84

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## MM58_UTF8 — Unicode mapping

Input / fixture:

```text
<student><name>సాయి</name></student>
```

Expected checks: {"xmlRoot": "customer", "contains": ["సాయి"]}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version="1.0" encoding="UTF-8"?>
<customer><fullName>సాయి</fullName></customer>
```

Java type: [B; bytes: 93

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## MM58_EMPTY — Empty source field

Input / fixture:

```text
<student><name/></student>
```

Expected checks: {"xmlRoot": "customer"}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version="1.0" encoding="UTF-8"?>
<customer><fullName></fullName></customer>
```

Java type: [B; bytes: 81

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## MM58_ESCAPE — Escaped XML data

Input / fixture:

```text
<student><name>A &amp; B</name></student>
```

Expected checks: {"xmlRoot": "customer", "contains": ["A & B"]}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version="1.0" encoding="UTF-8"?>
<customer><fullName>A &amp; B</fullName></customer>
```

Java type: [B; bytes: 90

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## MM58_BAD — Malformed XML rejected

Input / fixture:

```text
<student>
```

Expected checks: {"negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
com.sap.aii.utilxi.misc.api.BaseRuntimeException: XML document structures must start and end within the same entity. on the exchange: Exchange[C0CD96304BA75D5-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### Message Mapping

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Reference Type | static | Static, Dynamic | When applicable |
| Processing | Expression |  | Expression must start with one of the following values: '${header.', '${headers.', '${in.header.', '${in.headers.', '${property.', '${exchangeProperty.', 'pd:'. | Conditional |
| Processing | mappingname | StudentToCustomer |  | Conditional |
| Processing | Resource | /StudentToCustomer.mmap | Open mapping editor to view resource | Conditional |
| Processing | mappinguri | dir://mmap/src/main/resources/mapping/StudentToCustomer.mmap |  | Conditional |
| Processing | messageMappingBundleId |  | Message mapping artifact bundle id. | Conditional |

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/LoadLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [813 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: [{"severity": "Warning", "message": "Script may not pass XML or JSON message to Message Mapping. Message Mapping supports XML or JSON input only.", "elementId": "CallActivity_100", "elementType": "CallActivity", "location": "base - Message Mapping", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 058 - Message Mapping.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}]

# TRN 059 - Externalized Parameter

Technical ID: IF_TRN_059_Externalized_Parameter. Version 3.0.0.

Default Configure → ScenarioId=PARAM59_DEFAULT. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| PARAM59_DEFAULT | Externalized greeting prefix and scenario | base | SIMULATION: PASS |
| PARAM59_UTF8 | Unicode student | base | SIMULATION: PASS |
| PARAM59_EMPTY | Empty optional student | base | SIMULATION: PASS |
| PARAM59_OTHER | Another configured scenario | base | SIMULATION: PASS |

## PARAM59_DEFAULT — Externalized greeting prefix and scenario

Input / fixture:

```text
[empty body]
```

Properties: {"StudentName": "Anu"}

Expected checks: {"greeting": true}

Recorded: SIMULATION / PASS

Native output:

```text
Hello, Anu!
```

Java type: java.lang.String; bytes: 11

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## PARAM59_UTF8 — Unicode student

Input / fixture:

```text
[empty body]
```

Properties: {"StudentName": "సాయి"}

Expected checks: {"greeting": true}

Recorded: SIMULATION / PASS

Native output:

```text
Hello, సాయి!
```

Java type: java.lang.String; bytes: 20

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## PARAM59_EMPTY — Empty optional student

Input / fixture:

```text
[empty body]
```

Properties: {"StudentName": ""}

Expected checks: {"greeting": true}

Recorded: SIMULATION / PASS

Native output:

```text
Hello, !
```

Java type: java.lang.String; bytes: 8

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## PARAM59_OTHER — Another configured scenario

Input / fixture:

```text
[empty body]
```

Properties: {"StudentName": "Ravi"}

Expected checks: {"greeting": true}

Recorded: SIMULATION / PASS

Native output:

```text
Hello, Ravi!
```

Java type: java.lang.String; bytes: 12

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.GreetingPrefix}, ${property.StudentName}! | Enter an expression or a constant to form new message body | When applicable |

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/LoadLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

Saved design messages: []

# TRN 060 - CMS Encrypt Decrypt

Technical ID: IF_TRN_060_CMS_Encrypt_Decrypt. Version 3.0.0.

Default Configure → ScenarioId=SEC60_TEXT. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| SEC60_TEXT | Security processing of business text | base | SIMULATION: PASS |
| SEC60_UTF8 | UTF-8 security payload | base | SIMULATION: PASS |
| SEC60_JSON | Structured JSON payload | base | SIMULATION: PASS |
| SEC60_MULTI | Preserve message bytes | base | SIMULATION: PASS |
| SEC60_TAMPER | Corrupted encrypted/signed message rejected | base | SIMULATION: EXPECTED_REJECTION |

## SEC60_TEXT — Security processing of business text

Input / fixture:

```text
Order SO1001, amount 250
```

Expected checks: {"exact": "Order SO1001, amount 250"}

Recorded: SIMULATION / PASS

Native output:

```text
Order SO1001, amount 250
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 24

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SEC60_UTF8 — UTF-8 security payload

Input / fixture:

```text
José సాయి 😀
```

Expected checks: {"exact": "José సాయి 😀"}

Recorded: SIMULATION / PASS

Native output:

```text
José సాయి 😀
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 23

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SEC60_JSON — Structured JSON payload

Input / fixture:

```text
{"orderId":"SO1001"}
```

Expected checks: {"exact": "{\"orderId\":\"SO1001\"}"}

Recorded: SIMULATION / PASS

Native output:

```text
{"orderId":"SO1001"}
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 20

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SEC60_MULTI — Preserve message bytes

Input / fixture:

```text
line1
line2
```

Expected checks: {"exact": "line1\nline2"}

Recorded: SIMULATION / PASS

Native output:

```text
line1
line2
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 11

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SEC60_TAMPER — Corrupted encrypted/signed message rejected

Input / fixture:

```text
Order SO1001
```

Expected checks: {"tamper": true, "negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
iaik.asn1.DerInputException: Indefinite length: second byte not 0! on the exchange: Exchange[9A02428F654E785-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### PKCS7/CMS Decryptor

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Body is Base64 Encoded | false | Select this to specify if the body of the incoming message is Base64 encoded | When applicable |
| Processing | Signatures | EnvelopedData | Enveloped Data Only, Enveloped or "Signed and Enveloped" Data, Signed and Enveloped Data | When applicable |
| Processing | Public Key Alias | [] |  | Conditional |
| Processing | Public Key Alias |  | Enter the public key aliases of all expected senders. The public keys are used to verify the signature(s). You can also enter ${header.headername} or ${property.propertyname} to dynamically read the a … [240 characters; full value in scenario/evidence JSON] | When applicable |

### PKCS7/CMS Encryptor

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Block Size(in bytes) | 2048 | Enter block size of data to be encoded. Whole data is encoded for block size less than or equal to zero | When applicable |
| Processing | Encode Body with Base64 | false | Select this if you want the message body to be encoded with Base64. | When applicable |
| Processing | Signatures | enveloped | Enveloped Data Only, Signed and Enveloped Data | When applicable |
| Processing | Content Encryption Algorithm | AES/CBC/PKCS5Padding | AES/CBC/PKCS5Padding, AES/CCM/NoPadding, AES/GCM/NoPadding, ARCFOUR/ECB/NoPadding, Camellia/CBC/PKCS5Padding, DES/CBC/PKCS5Padding, DESede/CBC/PKCS5Padding, RC2/CBC/PKCS5Padding, CAST5/CBC/PKCS5Paddin … [201 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | Secret Key Length | 256 | 40, 48, 56, 64, 72, 80, 88, 96, 104, 112, 120, 128, 192, 256 | When applicable |
| Processing | Receiver Public Key Alias | [{"Public Key Alias": {"value": "trn_canvas_rsa", "defaultValue": "", "key": ""}}] |  | When applicable |
| Processing | Public Key Alias |  |  | When applicable |
| Processing | Signer Parameters | [] |  | Conditional |
| Processing | Private Key Alias |  | Enter an alias for selecting a private key from keystore | When applicable |
| Processing | Signature Algorithm |  | SHA512/RSA, SHA384/RSA, SHA256/RSA, SHA224/RSA, SHA/RSA, RIPEMD128/RSA, RIPEMD160/RSA, RIPEMD256/RSA, MD5/RSA | When applicable |
| Processing | Include Certificates |  | true, false | When applicable |

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [815 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: [{"severity": "Warning", "message": "Script may not pass Binary message to Decryptor. Decryptor supports Binary input only.", "elementId": "CallActivity_102", "elementType": "CallActivity", "location": "base - PKCS7", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 060 - CMS Encrypt Decrypt.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}]

# TRN 061 - CMS Sign Verify

Technical ID: IF_TRN_061_CMS_Sign_Verify. Version 3.0.0.

Default Configure → ScenarioId=SEC61_TEXT. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| SEC61_TEXT | Security processing of business text | base | SIMULATION: PASS |
| SEC61_UTF8 | UTF-8 security payload | base | SIMULATION: PASS |
| SEC61_JSON | Structured JSON payload | base | SIMULATION: PASS |
| SEC61_MULTI | Preserve message bytes | base | SIMULATION: PASS |
| SEC61_TAMPER | Corrupted encrypted/signed message rejected | base | SIMULATION: EXPECTED_REJECTION |

## SEC61_TEXT — Security processing of business text

Input / fixture:

```text
Order SO1001, amount 250
```

Expected checks: {"exact": "Order SO1001, amount 250"}

Recorded: SIMULATION / PASS

Native output:

```text
Order SO1001, amount 250
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 24

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SEC61_UTF8 — UTF-8 security payload

Input / fixture:

```text
José సాయి 😀
```

Expected checks: {"exact": "José సాయి 😀"}

Recorded: SIMULATION / PASS

Native output:

```text
José సాయి 😀
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 23

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SEC61_JSON — Structured JSON payload

Input / fixture:

```text
{"orderId":"SO1001"}
```

Expected checks: {"exact": "{\"orderId\":\"SO1001\"}"}

Recorded: SIMULATION / PASS

Native output:

```text
{"orderId":"SO1001"}
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 20

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SEC61_MULTI — Preserve message bytes

Input / fixture:

```text
line1
line2
```

Expected checks: {"exact": "line1\nline2"}

Recorded: SIMULATION / PASS

Native output:

```text
line1
line2
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 11

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SEC61_TAMPER — Corrupted encrypted/signed message rejected

Input / fixture:

```text
Order SO1001
```

Expected checks: {"tamper": true, "negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
iaik.cms.InvalidContentHashException: Content hash does not match to MessageDigest attribute value! on the exchange: Exchange[47E9643D970812D-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### PKCS7 Signature Verifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Header is Base64 Encoded | false | Select this option to verify if the Signed Data encoded in Base 64 is included in the header | When applicable |
| Processing | Body is Base64 Encoded | false | Select this option to verify if the Signed Data encoded in Base 64 is included in the message body | When applicable |
| Processing | Public Key Aliases | [{"publicKeyAlias": {"value": "trn_canvas_rsa", "defaultValue": "", "key": ""}}] | Enter an alias name to select a public key and corresponding certificate from the keystore. You can also enter ${header.headername} or ${property.propertyname} to dynamically read the alias from a hea … [225 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | publicKeyAlias |  |  | When applicable |

### PKCS7/CMS Signer

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Block Size (in bytes) | 2048 | Enter block size of data to be encoded. For block size less than or equal to zero, whole data is encoded. | When applicable |
| Processing | Include Content in Signed Data | true | Include original content within Signed Data and write the Signed Data into message body.<br>Deselect this to write Signed Data to property 'SapCmsSignedData', and retain original content in message body. | When applicable |
| Processing | OID for Content Type | 1.2.840.113549.1.7.1 | 1.2.840.113549.1.7.1 (Data), 1.2.840.113549.1.7.5 (Digested Data) | Conditional |
| Processing | Encode Signed Data with Base64 | false | Select this to encode the signed data with Base64 | When applicable |
| Processing | Signer Parameters: | [{"Include Certificates": {"value": "true", "defaultValue": "", "key": ""}, "Include Signing Time": {"value": "false", "defaultValue": "", "key": ""}, "Signatur … [311 characters; full value in scenario/evidence JSON] |  | When applicable |
| Processing | Private Key Alias |  | Enter an alias for selecting a private key from keystore | When applicable |
| Processing | Signature Algorithm |  | SHA512/RSA, SHA384/RSA, SHA256/RSA, SHA224/RSA, SHA3-512/RSA, SHA3-384/RSA, SHA3-256/RSA, SHA3-224/RSA, SHA/RSA, RIPEMD128/RSA, RIPEMD160/RSA, RIPEMD256/RSA, MD5/RSA, MD2/RSA, RIPEMD160andMGF1/RSA-ISO … [491 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | Include Certificates |  | true, false | When applicable |
| Processing | Include Signing Time |  | true, false | When applicable |

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [815 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: []

# TRN 062 - Simple Signer

Technical ID: IF_TRN_062_Simple_Signer. Version 3.0.0.

Default Configure → ScenarioId=SEC62_TEXT. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| SEC62_TEXT | Security processing of business text | base | SIMULATION: PASS |
| SEC62_UTF8 | UTF-8 security payload | base | SIMULATION: PASS |
| SEC62_JSON | Structured JSON payload | base | SIMULATION: PASS |
| SEC62_MULTI | Preserve message bytes | base | SIMULATION: PASS |

## SEC62_TEXT — Security processing of business text

Input / fixture:

```text
Order SO1001, amount 250
```

Expected checks: {"exact": "Order SO1001, amount 250", "signatureHeader": "SAPSimpleSignatureValue"}

Recorded: SIMULATION / PASS

Native output:

```text
Order SO1001, amount 250
```

Java type: java.lang.String; bytes: 24

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SEC62_UTF8 — UTF-8 security payload

Input / fixture:

```text
José సాయి 😀
```

Expected checks: {"exact": "José సాయి 😀", "signatureHeader": "SAPSimpleSignatureValue"}

Recorded: SIMULATION / PASS

Native output:

```text
José సాయి 😀
```

Java type: java.lang.String; bytes: 23

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SEC62_JSON — Structured JSON payload

Input / fixture:

```text
{"orderId":"SO1001"}
```

Expected checks: {"exact": "{\"orderId\":\"SO1001\"}", "signatureHeader": "SAPSimpleSignatureValue"}

Recorded: SIMULATION / PASS

Native output:

```text
{"orderId":"SO1001"}
```

Java type: java.lang.String; bytes: 20

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SEC62_MULTI — Preserve message bytes

Input / fixture:

```text
line1
line2
```

Expected checks: {"exact": "line1\nline2", "signatureHeader": "SAPSimpleSignatureValue"}

Recorded: SIMULATION / PASS

Native output:

```text
line1
line2
```

Java type: java.lang.String; bytes: 11

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### Simple Signer

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Private Key Alias | trn_canvas_rsa | Enter an alias for selecting a private key from keystore. You can also enter ${header.headername} or ${property.propertyname} to dynamically read the alias from a header or exchange property. | When applicable |
| Processing | Signature Algorithm | SHA256/RSA | SHA512/RSA, SHA384/RSA, SHA256/RSA, SHA224/RSA, SHA/RSA, RIPEMD128/RSA, RIPEMD160/RSA, RIPEMD256/RSA, MD5/RSA, MD2/RSA, RIPEMD160andMGF1/RSA-ISO9796-2-2-3, SHAandMGF1/RSA-ISO9796-2-2-3, SHA256withDSA, … [491 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | Signature Header Name | SAPSimpleSignatureValue | Enter the name of the message header where the resulting signature value will be set | When applicable |

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/LoadLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [815 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: []

# TRN 063 - XML Sign Verify

Technical ID: IF_TRN_063_XML_Sign_Verify. Version 3.0.0.

Default Configure → ScenarioId=SEC63_XML. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| SEC63_XML | Sign/verify XML | base | SIMULATION: PASS |
| SEC63_UTF8 | Unicode signed XML | base | SIMULATION: PASS |
| SEC63_TAMPER | Tampered XML fails verification | base | SIMULATION: EXPECTED_REJECTION |
| SEC63_BAD | Malformed XML rejected before signing | base | SIMULATION: EXPECTED_REJECTION |

## SEC63_XML — Sign/verify XML

Input / fixture:

```text
<student><name>Anu</name></student>
```

Expected checks: {"xmlEquivalent": "<student><name>Anu</name></student>"}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version="1.0" encoding="UTF-8"?><student><name>Anu</name></student>
```

Java type: [B; bytes: 73

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SEC63_UTF8 — Unicode signed XML

Input / fixture:

```text
<student><name>సాయి</name></student>
```

Expected checks: {"xmlLeafValues": ["సాయి"]}

Recorded: SIMULATION / PASS

Native output:

```text
<?xml version="1.0" encoding="UTF-8"?><student><name>సాయి</name></student>
```

Java type: [B; bytes: 82

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SEC63_TAMPER — Tampered XML fails verification

Input / fixture:

```text
<student><name>Anu</name></student>
```

Expected checks: {"tamper": true, "negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
Signature validation failed. The calculated digest value of the document  #_2f421747-739a-41ab-9322-62a7c38353aa is not equal to the value specified in the XML signature. The document may have been tampered. on the exchange: Exchange[6651185E838F3AE-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SEC63_BAD — Malformed XML rejected before signing

Input / fixture:

```text
<student>
```

Expected checks: {"negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
org.xml.sax.SAXParseException; lineNumber: 1; columnNumber: 10; XML document structures must start and end within the same entity. on the exchange: Exchange[7ABACCA02C44BBA-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### XML Signature Verifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Expected Signature Type | Enveloping or Enveloped XML Signature | Enveloping or Enveloped XML Signature, Detached XML Signatures | When applicable |
| Processing | XML Schema File Path |  | Select the XML schema file in the xsd folder. If the folder is empty insert the XML schema file. | Conditional |
| Processing | Check for Key Info Element | false | Select this option to check that the XML Signature contains a Key Info element | When applicable |
| Processing | Exclude XML Declaration | false | Select this option to exclude the XML declaration header in the outgoing XML message. For example, <?xml version=\"1.0\"; encoding=\"UTF-8"\> will be excluded. | When applicable |
| Processing | Disallow DOCTYPE Declaration | true | Select this option to disallow DTD DOCTYPE declaration in the incoming XML message | When applicable |
| Processing | Public Key Aliases | [{"publicKeyAlias": {"value": "trn_canvas_rsa", "defaultValue": "", "key": ""}}] | Enter an alias name to select a public key and corresponding certificate from the keystore | When applicable |
| Processing | publicKeyAlias |  |  | When applicable |

### XML Signer

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Private Key Alias | trn_canvas_rsa | Enter an alias for selecting a private key from keystore. You also can enter ${header.headername} to dynamically read the name from a header. With ${property.propertyname} you can dynamically read the … [238 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | Signature Algorithm | SHA256/RSA | SHA1/DSA, SHA1/RSA, SHA256/RSA, SHA384/RSA, SHA512/RSA, SHA224/ECDSA, SHA256/ECDSA, SHA384/ECDSA, SHA512/ECDSA | When applicable |
| Processing | Digest Algorithm |  | From Signature Algorithm, SHA1, SHA256, SHA384, SHA512 | When applicable |
| Processing | Signature Type | Enveloping XML signature | Enveloping XML signature, Enveloped XML signature, Detached XML signatures | When applicable |
| Processing | Parent Node | Specified by Name and Namespace | Specified by Name and Namespace, Specified by XPath expression | Conditional |
| Processing | Name |  | For enveloped signature, enter the parent element name within which the signature is inserted | Conditional |
| Processing | Namespace |  | For enveloped signature, enter the namespace of parent element of the signature | Conditional |
| Processing | XPath Expression |  | Specify the parent node for including the signature via XPath expression. | Conditional |
| Processing | XML Schema File Path |  | Select the XML schema in the XSD folder. If the folder is empty, insert the XML schema file. | Conditional |
| Processing | Signatures for Elements | [] | Enter the XPath to the attribute of type ID, in order to identify the element to be signed. Example: /nsx:Document/SubDocument/@Id. Namespace prefixes must be defined in Namespace Mapping of the Runti … [217 characters; full value in scenario/evidence JSON] | Conditional |
| Processing | Xpath to ID Attribute |  |  | When applicable |
| Processing | X.509 Certificate Chain | true | Select this option to add the X.509 certificate chain of the signer key to the Key Info | When applicable |
| Processing | X.509 Certificate | false | Select this option to add the X.509 certificate of the signer key to the Key Info | When applicable |
| Processing | Issuer Distinguished Name and Serial Number | false | Select this option to add the issuer distinguished name and the serial number of the signer X.509 certificate to the Key Info | When applicable |
| Processing | Key Value | false | Select this option to add the key value (exponent and modulus) of the signer key to the Key Info | When applicable |
| Processing | Sign Key Info | false | Select this option to include the Key Info into the signing process | When applicable |
| Advanced | Canonicalization Method for SignedInfo | Inclusive XML Canonicalization | XML Canonicalization Version 1.0, XML Canonicalization with Comments Version 1.0, Exclusive XML Canonicalization Version 1.0, Exclusive XML Canonicalization with Comments Version 1.0, XML Canonicaliza … [264 characters; full value in scenario/evidence JSON] | When applicable |
| Advanced | Transform Method for Payload | Inclusive XML Canonicalization | XML Canonicalization Version 1.0, XML Canonicalization with Comments Version 1.0, Exclusive XML Canonicalization Version 1.0, Exclusive XML Canonicalization with Comments Version 1.0, XML Canonicaliza … [264 characters; full value in scenario/evidence JSON] | When applicable |
| Advanced | Reference Type |  | Reference Type helps in the processing the referenced data, where Reference represents the object to be signed. For example, if the Reference is an Object, use the Reference Type as "http://www.w3.org … [224 characters; full value in scenario/evidence JSON] | When applicable |
| Advanced | Namespace Prefix | ds | Enter a namespace prefix for the XML signature elements | When applicable |
| Advanced | Signature ID |  | If required specify a Signature Id. Otherwise no Signature Id attribute will be generated. | When applicable |
| Advanced | Output Encoding | UTF-8 | Select encoding from the list | When applicable |
| Advanced | Exclude XML Declaration | false | Select this option to exclude the XML declaration header in the outgoing XML message. For example, <?xml version="1.0" encoding="UTF-8"?> will be excluded. | When applicable |
| Advanced | Disallow DOCTYPE Declaration | true | Select this option to disallow DTD DOCTYPE declaration in the incoming XML message | When applicable |
| XAdES-BES(1) | Time | false | Select this option to add the signing time | When applicable |
| XAdES-BES(1) | Certificate | None | None, Certificate Only, Certificate Chain | When applicable |
| XAdES-BES(1) | Digest Algorithm | SHA-512 | SHA1, SHA256, SHA384, SHA512 | Conditional |
| XAdES-BES(1) | xadesCertificateUri | [] | Enter the location of the referenced certificate(s) | Conditional |
| XAdES-BES(1) | Certificate URI |  |  | When applicable |
| XAdES-BES(1) | xadesSignerRoleClaimedRoles | [] | Enter the roles of the signer that do not have certificates to corroborate this claim. It can be either text or an XML fragment with the root element "ClaimedRole" | When applicable |
| XAdES-BES(1) | Claimed Role |  |  | When applicable |
| XAdES-BES(1) | Certified Roles | [] | Enter the roles with attribute certificates for the signer | When applicable |
| XAdES-BES(1) | Attribute Certificate (Base64 Encoded) |  |  | When applicable |
| XAdES-BES(1) | Encoding |  |  | When applicable |
| XAdES-BES(1) | ID |  |  | When applicable |
| XAdES-BES(1) | Description |  | Enter a description of the format of the data to be signed | When applicable |
| XAdES-BES(1) | MIME Type |  | Enter the MIME type of the format of the data to be signed | When applicable |
| XAdES-BES(1) | Name |  | Enter an identifier indicating the format of the data to be signed (URI or OID) | When applicable |
| XAdES-BES(1) | Qualifier |  | , OIDAsURI, OIDAsURN | When applicable |
| XAdES-BES(1) | Description |  | Enter a description of the identifier | When applicable |
| XAdES-BES(1) | xadesIdentifierDocumentationReference | [] | Enter references (URIs) pointing to further explanatory documentation of the identifier | When applicable |
| XAdES-BES(1) | Documentation Reference |  |  | When applicable |
| XAdES-BES(2) | City |  | Enter the city of the signer | When applicable |
| XAdES-BES(2) | State/Province |  | Enter the state or province of the signer | When applicable |
| XAdES-BES(2) | Postal Code |  | Enter the postal code of the signer | When applicable |
| XAdES-BES(2) | Country |  | Enter the country of the signer | When applicable |
| XAdES-BES(2) | Commitment |  | , Proof of Origin, Proof of Receipt, Proof of Delivery, Proof of Sender, Proof of Approval, Proof of Creation | When applicable |
| XAdES-BES(2) | Description |  | Enter a description of the commitment | Conditional |
| XAdES-BES(2) | commitmentDocumentationReferenceTable | [] | Enter references (URIs) pointing to further explanatory documentation of the commitment | Conditional |
| XAdES-BES(2) | Documentation Reference |  |  | When applicable |
| XAdES-BES(2) | commitmentQualifierTable | [] | Enter additional information qualifying the commitment. It can be either a text or a XML Fragment with the root element 'CommitmentTypeQualifier'. | Conditional |
| XAdES-BES(2) | Commitment Qualifier |  |  | When applicable |
| XAdES-BES(2) | Namespace |  | , http://uri.etsi.org/01903/v1.1.1#, http://uri.etsi.org/01903/v1.2.2#, http://uri.etsi.org/01903/v1.3.2# | When applicable |
| XAdES-BES(2) | Namespace Prefix | etsi | Enter a prefix of the XAdES namespace | When applicable |
| XAdES-EPES | Signature Policy | None | None, Implied, Explicit ID | When applicable |
| XAdES-EPES | Identifier |  | Enter an identifier that uniquely identifies a specific version of the signature policy (URI or OID) | Conditional |
| XAdES-EPES | Identifier Qualifier |  | , OIDAsURI, OIDAsURN | Conditional |
| XAdES-EPES | Description |  | Enter a description of the signature policy | Conditional |
| XAdES-EPES | xadesDocumentationReferenceTab | [] | Enter references (URIs) pointing to further explanatory documentation of the signature policy version | Conditional |
| XAdES-EPES | Documentation Reference |  |  | When applicable |
| XAdES-EPES | Digest Algorithm | SHA-512 | SHA1, SHA256, SHA384, SHA512 | Conditional |
| XAdES-EPES | Digest Value |  | Enter the base64 encoded digest value of the signature policy document | Conditional |
| XAdES-EPES | xadesPolicyQualifierTab | [] | Enter additional information qualifying the signature policy. It can be either a text or a XML Fragment with the root element 'SigPolicyQualifier'. | Conditional |
| XAdES-EPES | Policy Qualifier |  |  | When applicable |

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [813 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: [{"severity": "Warning", "message": "Script may not pass XML message to XML Signer. XML Signer supports XML input only.", "elementId": "CallActivity_100", "elementType": "CallActivity", "location": "base - XML Signer", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 063 - XML Sign Verify.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}, {"severity": "Warning", "message": "Script may not pass XML message to XML Verifier. XML Verifier supports XML input only.", "elementId": "CallActivity_102", "elementType": "CallActivity", "location": "base - XML Signature Verifier", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 063 - XML Sign Verify.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}]

# TRN 064 - PGP Encrypt Decrypt

Technical ID: IF_TRN_064_PGP_Encrypt_Decrypt. Version 3.0.0.

Default Configure → ScenarioId=SEC64_TEXT. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| SEC64_TEXT | Security processing of business text | base | SIMULATION: PASS |
| SEC64_UTF8 | UTF-8 security payload | base | SIMULATION: PASS |
| SEC64_JSON | Structured JSON payload | base | SIMULATION: PASS |
| SEC64_MULTI | Preserve message bytes | base | SIMULATION: PASS |
| SEC64_TAMPER | Corrupted encrypted/signed message rejected | base | SIMULATION: EXPECTED_REJECTION |

## SEC64_TEXT — Security processing of business text

Input / fixture:

```text
Order SO1001, amount 250
```

Expected checks: {"exact": "Order SO1001, amount 250"}

Recorded: SIMULATION / PASS

Native output:

```text
Order SO1001, amount 250
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 24

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SEC64_UTF8 — UTF-8 security payload

Input / fixture:

```text
José సాయి 😀
```

Expected checks: {"exact": "José సాయి 😀"}

Recorded: SIMULATION / PASS

Native output:

```text
José సాయి 😀
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 23

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SEC64_JSON — Structured JSON payload

Input / fixture:

```text
{"orderId":"SO1001"}
```

Expected checks: {"exact": "{\"orderId\":\"SO1001\"}"}

Recorded: SIMULATION / PASS

Native output:

```text
{"orderId":"SO1001"}
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 20

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SEC64_MULTI — Preserve message bytes

Input / fixture:

```text
line1
line2
```

Expected checks: {"exact": "line1\nline2"}

Recorded: SIMULATION / PASS

Native output:

```text
line1
line2
```

Java type: org.apache.camel.converter.stream.InputStreamCache; bytes: 11

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SEC64_TAMPER — Corrupted encrypted/signed message rejected

Input / fixture:

```text
Order SO1001
```

Expected checks: {"tamper": true, "negative": true}

Recorded: SIMULATION / EXPECTED_REJECTION

```text
java.lang.IllegalArgumentException: The input message body has an invalid format. The PGP decryption/verification processor expects a sequence of PGP packets of the form (entries in brackets are optional and ellipses indicate repetition, comma represents  sequential composition, and vertical bar separates alternatives): Public Key Encrypted Session Key ..., Symmetrically Encrypted Data | Sym. Encrypted and Integrity Protected Data, Compressed Data, (One Pass Signature ...,) Literal Data, (Signature ...,) on the exchange: Exchange[F5C271D94B2A0FE-0000000000000001]
```

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### PGP Decryptor

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Signatures | no_signature_allowed | None Expected, Optional, Required | When applicable |
| Processing | Signer Key User IDs | [] |  | Conditional |
| Processing | Signer User ID of Key(s) from Public Keyring |  | Enter the public key User IDs of all expected senders. The public keys are used to verify the signature(s). You can also enter ${header.headername} or ${property.propertyname} to dynamically read the  … [243 characters; full value in scenario/evidence JSON] | When applicable |

### PGP Encryptor

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Signatures | NoSignature | None, Including | When applicable |
| Processing | Content Encryption Algorithm | AES | AES, Blowfish, CAST5, DESede, Twofish | When applicable |
| Processing | Secret Key Length | 256 | 128, 168, 192, 256 | When applicable |
| Processing | Compression Algorithm | ZLIB | ZLIB, ZIP, BZip2, Uncompressed | When applicable |
| Processing | Armored | true | Select this option if you want the resulting PGP Message to be encoded in Base 64 with a specific header | When applicable |
| Processing | Integrity Protected Data Packet | true | Select this option in case you want to create a 'Symmetrically Encrypted and Integrity Protected Data Packet' (otherwise a 'Symmetrically Encrypted Data Packet' is created) | When applicable |
| Processing | Encryption Key User IDs | [{"EncryptUserID": {"value": "trn-canvas@training.invalid", "defaultValue": "", "key": ""}}] | Enter a User ID or a part of a User ID to select the receiver public key(s) from Public Keyring | When applicable |
| Processing | Encryption User ID of Key(s) from Public Keyring |  | Enter a User ID or a part of a User ID to select the receiver public key(s) from Public Keyring. You can also enter ${header.headername} or ${property.propertyname} to dynamically read the User ID fro … [232 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | Signature Algorithm | SHA-512 | SHA-256, SHA-512, SHA-384, SHA-224, SHA-1, RIPE-MD/160, MD5 | Conditional |
| Processing | Signer Key User IDs | [] | Enter a User ID or a part of a User ID to select the signer private key(s) from Secret Keyring | Conditional |
| Processing | Signer User ID of Key(s) from Secret Keyring |  | Enter a User ID or a part of a User ID to select the signer private key(s) from Secret Keyring. You can also enter ${header.headername} or ${property.propertyname} to dynamically read the User ID from … [231 characters; full value in scenario/evidence JSON] | When applicable |

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [815 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: [{"severity": "Warning", "message": "Script may not pass Binary message to PGP Decryptor. PGP Decryptor supports Binary input only.", "elementId": "CallActivity_102", "elementType": "CallActivity", "attributeId": "iflow.advance.pgp.decrypt.tab", "location": "base - PGP Decryptor", "resourcePath": "src/main/resources/scenarioflows/integrationflow", "resourceName": "TRN 064 - PGP Encrypt Decrypt.iflw", "resourceRelativePath": "scenarioflows/integrationflow"}]

# TRN 065 - CMS Signature Splitter

Technical ID: IF_TRN_065_CMS_Signature_Splitter. Version 3.0.0.

Default Configure → ScenarioId=SEC65_TEXT. Save and deploy once.

Configuration branches and overrides: {"base": {}}

| Scenario | Practical example | Variant | Verification |
|---|---|---|---|
| SEC65_TEXT | Security processing of business text | base | SIMULATION: PASS |
| SEC65_UTF8 | UTF-8 security payload | base | SIMULATION: PASS |
| SEC65_JSON | Structured JSON payload | base | SIMULATION: PASS |
| SEC65_MULTI | Preserve message bytes | base | SIMULATION: PASS |

## SEC65_TEXT — Security processing of business text

Input / fixture:

```text
Order SO1001, amount 250
```

Expected checks: {"cmsParts": true}

Recorded: SIMULATION / PASS

Native output:

```text
Order SO1001, amount 250
```

Java type: [B; bytes: 24

Native output:

```text
0�\u0002b\u0006	*�H��\u000d\u0001\u0007\u0002��\u0002S0�\u0002O\u0002\u0001\u00011\u000f0\u000d\u0006	`�H\u0001e\u0003\u0004\u0002\u0001\u0005\u00000\u000b\u0006	*�H��\u000d\u0001\u0007\u00011�\u0002*0�\u0002&\u0002\u0001\u000109011\u00110\u000f\u0006\u0003U\u0004
\u0013\u0008Training1\u001c0\u001a\u0006\u0003U\u0004\u0003\u0013\u0013CPI Canvas Training\u0002\u0004Qo�~0\u000d\u0006	`�H\u0001e\u0003\u0004\u0002\u0001\u0005\u0000���0\u0018\u0006	*�H��\u000d\u0001	\u00031\u000b\u0006	*�H��\u000d\u0001\u0007\u00010/\u0006	*�H��\u000d\u0001	\u00041"\u0004 ��,\u000c�%�+�b�x\u0006��?A\u0004\�Io��E�%|��y&0v\u0006\u000b*�H��\u000d\u0001	\u0010\u0002/1g0e0c0a\u0004 4_\u0010�\u0011�+ѐ�ʰ7o\u0001�;��R�\u0013��s�����0=05�3011\u00110\u000f\u0006\u0003U\u0004
\u0013\u0008Training1\u001c0\u001a\u0006\u0003U\u0004\u0003\u0013\u0013CPI Canvas Training\u0002\u0004Qo�~0\u000d\u0006	*�H��\u000d\u0001\u0001\u000b\u0005\u0000\u0004�\u0001\u00009��Al,Ҭ��\u0005\u0008\u0006m\u000b\u0010�#��<���b\u0014Ғ=�=����નz\u0018\u0016\u0008�q�\u001fOq\u000f�������\u0007C̶�\u0006dZ�5�S\u0013���P�!ճ��,QS\u0013��\u001fq��\u0015�\�b�ɀ\u0000}h�{I���Q峣�0\u0019\u0012|�\u0007��#�C\u0003��Un\u0005�<��&��qñ�A*+wݣ&\u0017��)\u0006�遺�_\u001bàPx�b�Ɩs3��L9����N/��\u000d\u001f=\u0000�.J㲙\u0002~8��Ӧ�\u0014&\u000f=K�T��\u0018M�\u001e�\u0003�0gmF��\u0011?}\u000b�6����oXe\u0013Z#�\u0001���E�\u0008�\u000c\u001d
```

Java type: [B; bytes: 614

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SEC65_UTF8 — UTF-8 security payload

Input / fixture:

```text
José సాయి 😀
```

Expected checks: {"cmsParts": true}

Recorded: SIMULATION / PASS

Native output:

```text
José సాయి 😀
```

Java type: [B; bytes: 23

Native output:

```text
0�\u0002b\u0006	*�H��\u000d\u0001\u0007\u0002��\u0002S0�\u0002O\u0002\u0001\u00011\u000f0\u000d\u0006	`�H\u0001e\u0003\u0004\u0002\u0001\u0005\u00000\u000b\u0006	*�H��\u000d\u0001\u0007\u00011�\u0002*0�\u0002&\u0002\u0001\u000109011\u00110\u000f\u0006\u0003U\u0004
\u0013\u0008Training1\u001c0\u001a\u0006\u0003U\u0004\u0003\u0013\u0013CPI Canvas Training\u0002\u0004Qo�~0\u000d\u0006	`�H\u0001e\u0003\u0004\u0002\u0001\u0005\u0000���0\u0018\u0006	*�H��\u000d\u0001	\u00031\u000b\u0006	*�H��\u000d\u0001\u0007\u00010/\u0006	*�H��\u000d\u0001	\u00041"\u0004 '-�r\u0016B7\u0002J�[+p��WV\u000b�jr�T���ݨT&�m0v\u0006\u000b*�H��\u000d\u0001	\u0010\u0002/1g0e0c0a\u0004 4_\u0010�\u0011�+ѐ�ʰ7o\u0001�;��R�\u0013��s�����0=05�3011\u00110\u000f\u0006\u0003U\u0004
\u0013\u0008Training1\u001c0\u001a\u0006\u0003U\u0004\u0003\u0013\u0013CPI Canvas Training\u0002\u0004Qo�~0\u000d\u0006	*�H��\u000d\u0001\u0001\u000b\u0005\u0000\u0004�\u0001\u0000\u001aE!�l(1,e	�o��Ϫ�3>T+Ni��Hkea^�R����m\u001b��T�e\u0019��no*d3�l\u0017��mU'�?��\u000d���v{^�l<�\u0008}�Pɛ�WԌ\u0005mlnT���YF�ϕ\u0012e\u001dóI'R�h$�	w��c�\u0008U�ke\u0018�b\u0018�ʄ���^���\u0012\u000b�2�\u000e�\u001c��j��\u0002��\u000e�M�.��\u000c\u001f\u0007~lC\u001a���\u0017&<l�'�4h=\u001bl\u0005\u0000���\u0002�<	�e\u001a�\�&wq!�0*T�w���c�+�9�!ԃG\u001e�\u00005\u0001��\u000bL��$�W"��o���6e��g\u0013�j���
```

Java type: [B; bytes: 614

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SEC65_JSON — Structured JSON payload

Input / fixture:

```text
{"orderId":"SO1001"}
```

Expected checks: {"cmsParts": true}

Recorded: SIMULATION / PASS

Native output:

```text
{"orderId":"SO1001"}
```

Java type: [B; bytes: 20

Native output:

```text
0�\u0002b\u0006	*�H��\u000d\u0001\u0007\u0002��\u0002S0�\u0002O\u0002\u0001\u00011\u000f0\u000d\u0006	`�H\u0001e\u0003\u0004\u0002\u0001\u0005\u00000\u000b\u0006	*�H��\u000d\u0001\u0007\u00011�\u0002*0�\u0002&\u0002\u0001\u000109011\u00110\u000f\u0006\u0003U\u0004
\u0013\u0008Training1\u001c0\u001a\u0006\u0003U\u0004\u0003\u0013\u0013CPI Canvas Training\u0002\u0004Qo�~0\u000d\u0006	`�H\u0001e\u0003\u0004\u0002\u0001\u0005\u0000���0\u0018\u0006	*�H��\u000d\u0001	\u00031\u000b\u0006	*�H��\u000d\u0001\u0007\u00010/\u0006	*�H��\u000d\u0001	\u00041"\u0004 ���(��\u0002��%�k�Rg|�\u001bỲ}��v����	��0v\u0006\u000b*�H��\u000d\u0001	\u0010\u0002/1g0e0c0a\u0004 4_\u0010�\u0011�+ѐ�ʰ7o\u0001�;��R�\u0013��s�����0=05�3011\u00110\u000f\u0006\u0003U\u0004
\u0013\u0008Training1\u001c0\u001a\u0006\u0003U\u0004\u0003\u0013\u0013CPI Canvas Training\u0002\u0004Qo�~0\u000d\u0006	*�H��\u000d\u0001\u0001\u000b\u0005\u0000\u0004�\u0001\u0000\u000b\u00036��E|0�k�c\u0013
���`&Ϗ��4��\u0012�D���\u000b�e��R[���\u001e�\u001fo\u0013�j�k��u���z\u0019�'t\u000e\u0010G��ve���\u0011\u0010��S3d~�ܱZǵ\u0012�p�:�%�G\u0018\u001a-�����\u0005�\u0005�njt�H�C�G��\u0016g�\u001dj\u0000�[*���$�#�u%��ir�\u001f�_����K�\u0011�\u0000\u0018�.+�Шr8����궗ςw�f[Q����d,zN�d#R�ԑ\u001f;��J��L�:|�\u0013�\u0018�Fm屢\u0002�#��;�H�
o��Mu��$�\u0000��n����\u001c_\u0004��Hd�xț�
```

Java type: [B; bytes: 614

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## SEC65_MULTI — Preserve message bytes

Input / fixture:

```text
line1
line2
```

Expected checks: {"cmsParts": true}

Recorded: SIMULATION / PASS

Native output:

```text
line1
line2
```

Java type: [B; bytes: 11

Native output:

```text
0�\u0002b\u0006	*�H��\u000d\u0001\u0007\u0002��\u0002S0�\u0002O\u0002\u0001\u00011\u000f0\u000d\u0006	`�H\u0001e\u0003\u0004\u0002\u0001\u0005\u00000\u000b\u0006	*�H��\u000d\u0001\u0007\u00011�\u0002*0�\u0002&\u0002\u0001\u000109011\u00110\u000f\u0006\u0003U\u0004
\u0013\u0008Training1\u001c0\u001a\u0006\u0003U\u0004\u0003\u0013\u0013CPI Canvas Training\u0002\u0004Qo�~0\u000d\u0006	`�H\u0001e\u0003\u0004\u0002\u0001\u0005\u0000���0\u0018\u0006	*�H��\u000d\u0001	\u00031\u000b\u0006	*�H��\u000d\u0001\u0007\u00010/\u0006	*�H��\u000d\u0001	\u00041"\u0004 h3v␂�H,&Ut\��z\u001d��\u0010��b�´-�ƍ\u000f�0v\u0006\u000b*�H��\u000d\u0001	\u0010\u0002/1g0e0c0a\u0004 4_\u0010�\u0011�+ѐ�ʰ7o\u0001�;��R�\u0013��s�����0=05�3011\u00110\u000f\u0006\u0003U\u0004
\u0013\u0008Training1\u001c0\u001a\u0006\u0003U\u0004\u0003\u0013\u0013CPI Canvas Training\u0002\u0004Qo�~0\u000d\u0006	*�H��\u000d\u0001\u0001\u000b\u0005\u0000\u0004�\u0001\u0000	Iׂ�Õn�F&�\u0008W\u00113b�}�\u0010��m���nu��m�\u000d~ld\u00068D`iD\u0006����!\u0006:��ң9*�\u0013�"2�_�\u0008��\u0018�p�\u000c��Y�NDN1�hm^��f@3y�P��5��\u0003|D�܍� ��D��A�����m��@�Ny��c\u001c�A�nT�\u001b�u�\u0008/�V��U\u0006�\u000c�I�.s(���d4P�2��\u0002�_bw.[@����D�q�]g��Uj���;��\u0000�\u001f(��\u0016�ө��\u000cSk0И\u000e��l�\u001an���g�ϩ���Jn�L�zm�H�\u0014\u0004��:����E����
```

Java type: [B; bytes: 614

Student exercise: predict the result, identify the native step, run and compare; then change one value and explain the difference.

## Applicable component tabs and controls

### End Message

No additional component processing fields; inspect General and the surrounding message flow.

### Groovy Script

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Script File | /script/VerifyLabScenario.groovy | Select a script from your integration flow or a referenced script | When applicable |
| Processing | Script Function |  | Enter the function name that has to be initially called for script execution. | When applicable |
| Processing | Script Bundle ID |  | Script artifact bundle id. | When applicable |

### PKCS#7/CMS Splitter

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Payload File Name |  | Enter a name for the file that would contain the payload after the split process. E.g. payload_file_name | When applicable |
| Processing | Signature File Name |  | Enter a name with extension .sig for the file that would contain the signature after the split process. E.g. payload_file_name.SIG . | When applicable |
| Processing | Wrap by Content Info | true | Select this option to wrap the PKCS#7 Signed Data containing the signature into a PKCS#7 content. | When applicable |
| Processing | Payload First | true | Select this option if the payload should be the first returned message. | When applicable |
| Processing | Base64 Payload |  | Select this option to encode the payload with Base64 encoding scheme after the split. | When applicable |
| Processing | Base64 Signature |  | Select this option to encode the signature with Base64 encoding scheme after the split. | When applicable |

### PKCS7/CMS Signer

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Processing | Block Size (in bytes) | 2048 | Enter block size of data to be encoded. For block size less than or equal to zero, whole data is encoded. | When applicable |
| Processing | Include Content in Signed Data | true | Include original content within Signed Data and write the Signed Data into message body.<br>Deselect this to write Signed Data to property 'SapCmsSignedData', and retain original content in message body. | When applicable |
| Processing | OID for Content Type | 1.2.840.113549.1.7.1 | 1.2.840.113549.1.7.1 (Data), 1.2.840.113549.1.7.5 (Digested Data) | Conditional |
| Processing | Encode Signed Data with Base64 | false | Select this to encode the signed data with Base64 | When applicable |
| Processing | Signer Parameters: | [{"Include Certificates": {"value": "true", "defaultValue": "", "key": ""}, "Include Signing Time": {"value": "false", "defaultValue": "", "key": ""}, "Signatur … [311 characters; full value in scenario/evidence JSON] |  | When applicable |
| Processing | Private Key Alias |  | Enter an alias for selecting a private key from keystore | When applicable |
| Processing | Signature Algorithm |  | SHA512/RSA, SHA384/RSA, SHA256/RSA, SHA224/RSA, SHA3-512/RSA, SHA3-384/RSA, SHA3-256/RSA, SHA3-224/RSA, SHA/RSA, RIPEMD128/RSA, RIPEMD160/RSA, RIPEMD256/RSA, MD5/RSA, MD2/RSA, RIPEMD160andMGF1/RSA-ISO … [491 characters; full value in scenario/evidence JSON] | When applicable |
| Processing | Include Certificates |  | true, false | When applicable |
| Processing | Include Signing Time |  | true, false | When applicable |

### Timer Start

No additional component processing fields; inspect General and the surrounding message flow.

### Content Modifier

| Tab | Control | Saved example value | Choices / purpose | Visibility |
|---|---|---|---|---|
| Message Header | Headers | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "CPI", "default … [379 characters; full value in scenario/evidence JSON] |  | When applicable |
| Message Header | Action |  | Create, Delete | When applicable |
| Message Header | Name |  | Enter a name to define this header | Conditional |
| Message Header | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Message Header | Source Value |  | Define a value for a header based on the selected value type | Conditional |
| Message Header | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Message Header | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Exchange Property | Properties | [{"Action": {"value": "Create", "defaultValue": "", "key": ""}, "Type": {"value": "constant", "defaultValue": "", "key": ""}, "Value": {"value": "3.0", "default … [815 characters; full value in scenario/evidence JSON] |  | When applicable |
| Exchange Property | Action |  | Create, Delete | When applicable |
| Exchange Property | Name |  | Enter a name to define this property | Conditional |
| Exchange Property | Source Type |  | Constant, Header, XPath, Expression, Property, Local Variable, Global Variable, Number Range | Conditional |
| Exchange Property | Source Value |  | Define a value for a property based on the selected value type | Conditional |
| Exchange Property | Data Type |  | Enter a valid Java type for an Xpath value type.<br>E.g. java.lang.String | Conditional |
| Exchange Property | Default Value |  | Variable is not defined; please enter default value | Conditional |
| Message Body | Type | expression | Expression, Constant | When applicable |
| Message Body | Body | ${property.ScenarioId} | Enter an expression or a constant to form new message body | When applicable |

Saved design messages: []

# Verification records and limits

Full fixtures, assertions and measured reports are in tests/scenario-catalog.json and tests/evidence. Long/binary values are abbreviated here. Live message GUIDs identify actual executions; simulations are labelled. Earlier baseline and five-flow documents remain historical; this handbook supersedes their readiness statements for the remaining lessons.

Persistent state relationships independently checked: same ID Mapping source key returns the same ID; different key returns another ID. First idempotent call runs the local process, duplicate skips it, and a new key runs it. Existing keys remain persisted: clear the training entry or select a fresh key when repeating a first-call demonstration.

| Final default deployment | Outcome | Runtime status |
|---|---|---|
| IF_TRN_049_Local_Mock_Receiver | RECEIVER_STARTED | Receiver |
| IF_TRN_053_Aggregator | RECEIVER_STARTED | Receiver |
| IF_TRN_001_Hello_World | PASS | COMPLETED |
| IF_TRN_007_GZIP_Roundtrip | PASS | COMPLETED |
| IF_TRN_008_ZIP_Roundtrip | PASS | COMPLETED |
| IF_TRN_009_XML_Declaration | PASS | COMPLETED |
| IF_TRN_010_XML_to_CSV | PASS | COMPLETED |
| IF_TRN_011_XML_JSON_Roundtrip | PASS | COMPLETED |
| IF_TRN_012_Base64_Roundtrip | PASS | COMPLETED |
| IF_TRN_013_SHA256_Digest | PASS | COMPLETED |
| IF_TRN_014_General_Splitter_Gather | PASS | COMPLETED |
| IF_TRN_015_Iterating_Splitter_Gather | PASS | COMPLETED |
| IF_TRN_016_Line_Splitter_Gather | PASS | COMPLETED |
| IF_TRN_017_Data_Store_Roundtrip | PASS | COMPLETED |
| IF_TRN_018_Data_Store_Missing_Entry | PASS | FAILED |
| IF_TRN_019_Persist_Message | PASS | COMPLETED |
| IF_TRN_020_MIME_Roundtrip | PASS | COMPLETED |
| IF_TRN_021_Timer_Hello | PASS | COMPLETED |
| IF_TRN_022_Local_Process_Call | PASS | COMPLETED |
| IF_TRN_023_Router | PASS | COMPLETED |
| IF_TRN_024_Parallel_Multicast | PASS | COMPLETED |
| IF_TRN_025_Groovy_Uppercase | PASS | COMPLETED |
| IF_TRN_026_JavaScript_Uppercase | PASS | COMPLETED |
| IF_TRN_027_XSLT_Mapping | PASS | COMPLETED |
| IF_TRN_028_XML_Validator_Valid | PASS | COMPLETED |
| IF_TRN_029_XML_Validator_Invalid | PASS | FAILED |
| IF_TRN_030_CSV_to_XML | PASS | COMPLETED |
| IF_TRN_031_Sequential_Multicast | PASS | COMPLETED |
| IF_TRN_032_Looping_Process_Call | PASS | COMPLETED |
| IF_TRN_033_Loop_Maximum_Guard | PASS | FAILED |
| IF_TRN_034_Idempotent_Process_Call | PASS | COMPLETED |
| IF_TRN_035_Exception_Handled | PASS | COMPLETED |
| IF_TRN_036_Exception_Rethrown | PASS | FAILED |
| IF_TRN_037_Error_End_Event | PASS | FAILED |
| IF_TRN_038_Escalation_End_Event | PASS | ESCALATED |
| IF_TRN_039_Terminate_Message | PASS | COMPLETED |
| IF_TRN_040_Plain_End_Event | PASS | COMPLETED |
| IF_TRN_041_ID_Mapping | PASS | COMPLETED |
| IF_TRN_042_Write_Variables | PASS | COMPLETED |
| IF_TRN_043_Data_Store_Select | PASS | COMPLETED |
| IF_TRN_044_ZIP_Splitter | PASS | COMPLETED |
| IF_TRN_045_Tar_Splitter | PASS | COMPLETED |
| IF_TRN_046_Headers_Properties | PASS | COMPLETED |
| IF_TRN_047_XPath_Property | PASS | COMPLETED |
| IF_TRN_048_Request_Reply | PASS | COMPLETED |
| IF_TRN_050_Send_Local_Message | PASS | COMPLETED |
| IF_TRN_051_Content_Enricher | PASS | COMPLETED |
| IF_TRN_054_EDI_Extractor | PASS | COMPLETED |
| IF_TRN_055_EDI_Splitter | PASS | COMPLETED |
| IF_TRN_056_EDI_XML_Roundtrip | PASS | FAILED |
| IF_TRN_057_Aggregator_Trigger | PASS | COMPLETED |
| IF_TRN_058_Message_Mapping | PASS | COMPLETED |
| IF_TRN_059_Externalized_Parameter | PASS | COMPLETED |
| IF_TRN_060_CMS_Encrypt_Decrypt | PASS | COMPLETED |
| IF_TRN_061_CMS_Sign_Verify | PASS | COMPLETED |
| IF_TRN_062_Simple_Signer | PASS | COMPLETED |
| IF_TRN_063_XML_Sign_Verify | PASS | COMPLETED |
| IF_TRN_064_PGP_Encrypt_Decrypt | PASS | COMPLETED |
| IF_TRN_065_CMS_Signature_Splitter | PASS | COMPLETED |
