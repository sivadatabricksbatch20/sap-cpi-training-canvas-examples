# API Management and Event Mesh training

This extension adds five API Management labs and five exercises for each mesh product. Keep these separate from the 64 CPI canvas designs: an API proxy is not an iFlow, and a broker queue is not a CPI routing step.

## Recorded completion status

All five Classic proxies are coded, imported and deployed. **19 of the 21 scenarios passed on the native gateway.** Valid-key access and key revocation were not run because the user asked to stop further testing and finish the documentation. They require a dedicated Developer Hub app/key; their steps and expected outcomes remain in the catalogue. Developer Hub onboarding and the dedicated API product are prepared.

The ten mesh exercises remain prerequisite-blocked in this trial. Their clients, fixtures and instructions are supplied; local checks are labelled separately from native broker results. See [extension status](EXTENSION_STATUS.md) and the evidence JSON files for exact boundaries.

## Product responsibilities

| Product | Main purpose | Training example |
| --- | --- | --- |
| Cloud Integration | Transform, route and orchestrate messages between systems | Convert an order and send it to ERP |
| API Management | Expose, secure, control and observe APIs | Protect an order API with a key and quota |
| SAP Event Mesh | Deliver asynchronous events through topic subscriptions and queues | Notify billing after an order is created |
| Advanced event mesh | Manage event brokers and event-driven integration across environments | Route events between brokers and support operational messaging features |

A practical architecture is client → API proxy → integration/backend → event topic → subscribed queue → consumer. API gateway acceptance, broker acceptance and successful business processing are three different outcomes.

## API Management setup and workflow

API Management capability activation does not by itself configure its runtime. In this trial, API Modelling and Developer Hub were active, but Settings → Runtimes → Classic API Management initially showed Inactive. The runtime was activated for the training exercises. APIPortal.Administrator and APIManagement.SelfService.Administrator are assigned to the training account.

The dedicated package **API Management and Mesh - Training** (`APIManagementMeshTraining`) holds the handbook and source bundles. These five labs are native **Classic API Management proxies**, imported/deployed through the API portal management service. They are separate from the newer Integration Cell **Add → API** artifact format; that wizard rejects a Classic APIProxy ZIP. The repository stores native exported XML/script sources and the corresponding ZIP.

For another authorized tenant, obtain an API-access OAuth token and set `APIM_MANAGEMENT_BASE_URL` (ending `/apiportal/api/1.0`) and `APIM_MANAGEMENT_TOKEN` in the secure shell. Run `python api-management/deploy.py TRN_API_001_ProxyBasics`, then use the gateway tests. Each import may create/replace a native revision. No browser session credentials are part of this tool.

The synthetic, read-only backend is the public OData Northwind sample at `https://services.odata.org/V4/Northwind/Northwind.svc`. Its public sample data and availability are external dependencies. Send no private business information to it. The API-key lab removes the gateway key before forwarding the request.

Open **Configure → APIs** for Classic proxy editing (`/shell/configure/api/<proxy-name>`), **Engage** for products/applications, and **Test → APIs** for calls and debugging.

### First five API labs

| Lab | Practical examples | Main controls |
| --- | --- | --- |
| TRN_API_001_ProxyBasics | One/multiple products, query filtering, unknown resource | Proxy name/base path, target URL, resource suffix, query forwarding |
| TRN_API_002_Transform | One/multiple/empty results, course header | AssignMessage, Javascript, request/response flow placement |
| TRN_API_003_APIKey | Missing, invalid, valid and revoked key | VerifyAPIKey, product entitlement, application subscription, credential removal |
| TRN_API_004_Traffic | First three requests, fourth rejected, independent identifier, controlled burst | Quota, SpikeArrest, identifier, interval, distributed/synchronous counters, custom 429 |
| TRN_API_005_ValidationFaults | Valid count, zero/text/large count, wrong HTTP method | Conditional steps, request variables, Javascript validation, RaiseFault and consistent JSON errors |

The complete 21-case catalogue is `api-management/catalogue.json`. Quota uses `X-Training-Student` only to isolate classroom tests. A client can change that header; production quotas should use a trusted identity such as an authenticated application ID. Spike arrest smooths traffic and is not a business quota. Do not promise an exact accepted request count for a concurrent burst.

### Explain each screen and flow stage

| Area | What the learner should inspect |
| --- | --- |
| Overview / General | Stable name, description, version and intended audience |
| Proxy endpoint | Public base path, allowed request flow and route to target |
| Target endpoint / API provider | Backend URL, connectivity, TLS and authentication; gateway authentication is separate from backend authentication |
| Resources / API definition | Resource paths, methods, query parameters and documentation |
| Policies | Placement, order, conditions, enabled flag and `continueOnError`; inspect each XML setting |
| PreFlow request | Authentication, validation and traffic checks before forwarding |
| PostFlow response | Transformation and response headers after backend processing |
| Fault rules | Backend errors versus policy faults; return safe JSON and appropriate HTTP status |
| Deployment | Selected runtime, deployed revision and callable URL; saving alone does not deploy |
| Test / Debug / Trace | Request/response and policy execution; inspect variables without recording real credentials |
| Product / Developer Hub / application | Which APIs an app can call and which credential grants access |
| Analytics | Request counts, latency, faults and policy rejections; this differs from CPI MPL monitoring |

For the API-key lab create a dedicated training product containing only TRN_API_003_APIKey and its required resources. Subscribe a dedicated training app, approve access where required, then enter its key through the shell environment. Revoke only that app’s key for the revocation exercise.

```bash
export APIM_BASE_URL='https://e31930eatrial-trial.integrationsuitetrial-apim.ap21.hana.ondemand.com/e31930eatrial'
# Set APIM_TRAINING_API_KEY directly in your secure shell for the positive key case.
python api-management/run_tests.py --base-url "$APIM_BASE_URL" --output /tmp/apim-live-results.json
node api-management/test_policy_scripts.cjs
```

For this trial, the gateway base URL must include the project prefix `/e31930eatrial`; omitting it returns ApplicationNotFound even when metadata says DEPLOYED. `run_tests.py` rejects that gateway-routing fault rather than counting it as an expected resource 404. On another tenant, use its complete published URL/prefix.

`run_tests.py` calls the actual gateway. Local Javascript tests establish script logic only. Manual revocation and burst cases require their documented observations; they are not silently counted as native passes.

## SAP Event Mesh: five practical exercises

The trial has no SAP Event Mesh instance, and Event Mesh is absent from the available service-plan assignments. These exercises therefore require a service-enabled subaccount before native verification.

| Exercise | Setup and action | Expected evidence |
| --- | --- | --- |
| EM01 Publish/consume/acknowledge | Create a training queue and topic subscription. Publish SO1001, consume with QoS 1, then acknowledge using the returned message ID. | Same event ID and payload; empty queue after acknowledgement |
| EM02 Fan-out | Subscribe separate billing and shipping queues to the same topic. | Each queue receives its own copy |
| EM03 Competing consumers | Start two consumers for one queue; publish ten distinct IDs. | Ten unique events processed overall; not necessarily five per consumer |
| EM04 Offline durability and duplicates | Stop consumer, publish, restart; repeat the same event ID. | Queued delivery after restart; consumer idempotency prevents repeated business action |
| EM05 Retry/dead-letter handling | Configure supported broker retry/dead-letter behaviour and fail the handler. | Actual redelivery count and terminal destination, verified in that product |

Use your service’s own namespace, for example `<namespace>/orders/created/v1` and `<namespace>/q.orders`; do not publish in a production/SAP-reserved topic namespace. Create queues/subscriptions in the service UI and grant only the training addresses. This client does not modify broker administration.

```bash
export EM_SERVICE_KEY_FILE='/secure/local/path/service-key.json'
python event-mesh/client.py publish '<namespace>/orders/created/v1' --event event-mesh/order-created.json
python event-mesh/client.py consume '<namespace>/q.orders'
python event-mesh/client.py ack '<namespace>/q.orders' --message-id 'ID-FROM-THE-CONSUMPTION-RESPONSE'
python event-mesh/test_client.py
```

The client obtains an OAuth client-credentials token from the service key’s `httprest` messaging binding. It URL-encodes topic/queue/message IDs, publishes with `x-qos: 1`, and separates consumption from acknowledgement. Consume with care: it is an actual queue operation. The included test uses a local HTTP fixture and proves request construction/token reuse, not SAP queue durability or entitlement.

An application event ID is different from the broker’s transport message ID. Reusing an event ID does not automatically make the broker deduplicate it. Store a processed event ID in the same transactional boundary as the business update when the application supports that pattern.

## Advanced event mesh: five exercises

Advanced event mesh is also absent from the trial’s available service assignments. The repository includes a pinned local Solace broker Compose configuration for a supported Docker host. The current managed workspace could not start that broker because `/proc/sys/kernel/core_pattern` is unavailable. The image was not substituted with a fake implementation.

| Exercise | Setup and action | Expected evidence |
| --- | --- | --- |
| AEM01 Guaranteed delivery | Durable queue + topic subscription; persistent publish; manual accept | Broker acceptance, received event and removal after acknowledgement |
| AEM02 Wildcards/fan-out | Exact and wildcard topic subscriptions on separate queues | Matching events delivered and non-matching events excluded |
| AEM03 Competing consumers | Non-exclusive queue with two consumers | Work sharing; distribution and processing order explained |
| AEM04 Release/redelivery/DMQ | Release once, then accept redelivery; separately reach configured max redelivery | Redelivery and eventual dead-message handling using the broker’s configuration |
| AEM05 TTL and advanced design | Publish expiring training messages; inspect queue behaviour | Measured expiry; replay/DMR/HA require separately enabled features/resources |

Obtain the AMQPS host, port, complete username and password from the broker’s connection details. Copy the broker-provided topic/queue AMQP address format; SAP Event Mesh address conventions and Solace conventions are not interchangeable. Create a dedicated training client, durable queues, subscriptions and a non-exclusive access type where competing consumers are tested. Configure a named training DMQ and redelivery limits through the broker manager.

```bash
cd advanced-event-mesh
npm ci --ignore-scripts
# Set AEM_HOST, AEM_PORT, AEM_USERNAME and AEM_PASSWORD in the secure shell.
node client.cjs publish 'BROKER-TOPIC-ADDRESS' ../event-mesh/order-created.json
node client.cjs consume 'BROKER-QUEUE-ADDRESS'
node client.cjs release-once 'BROKER-QUEUE-ADDRESS'
node test_amqp.cjs
```

TLS certificate verification stays enabled for cloud connections. `AEM_LOCAL_PLAINTEXT=true` is allowed only with localhost/127.0.0.1 for a local exercise. Publish reports success only after broker acceptance. Consumers use manual acknowledgement; `release-once` releases the first delivery and waits for a subsequent delivery before accepting it. Whether/how that delivery is redelivered depends on the configured broker.

`test_amqp.cjs` runs a local AMQP wire-protocol fixture to verify publisher acceptance and manual receiver acknowledgement. It is not a Solace broker test or SAP advanced event mesh deployment. Replay logs, Dynamic Message Routing, clustering/HA, multi-region failover and cloud administration are explained extensions requiring provisioned capabilities; they are not claimed tested here.

### Optional local broker on a supported Docker machine

```bash
# Set AEM_ADMIN_PASSWORD in the shell; never commit it.
docker compose -f advanced-event-mesh/compose.yaml up -d
```

Open `http://localhost:18080`, configure a training client and queue/subscription, then use local AMQP port 15672. Ports bind to loopback. The default Compose service is a local broker, not a SAP cloud tenant. Stop the lab when finished. Broker upgrades, persistence/volumes and production HA are outside this disposable local exercise.

## Verification and Git standards

Every result must name its level: native SAP gateway, native SAP Event Mesh, native advanced event mesh, local Javascript unit test, HTTP contract fixture or AMQP protocol fixture. A prepared source file is not a successful deployment. Preserve blocked prerequisites and actual errors in the status manifest.

Use stable TRN_API/EM/AEM identifiers, synthetic fixtures, externalized connection values, explicit expected errors and least-privilege training resources. Keep service keys, access tokens, API keys and broker credentials outside Git. Export matching saved/deployed proxy revisions, keep sources and fixtures together, validate, then use a feature branch and review PR. UAT/PROD promotion and native SAP Git association remain separate project setup.

References: [SAP API Management recipes](https://github.com/SAP-archive/apibusinesshub-api-recipes), [SAP Event Mesh client examples](https://github.com/SAP-samples/event-mesh-client-nodejs-samples), [SAP Event Mesh REST messaging specification](https://help.sap.com/doc/3dfdf81b17b744ea921ce7ad464d1bd7/Cloud/en-US/messagingrest-api-spec.html), [Solace AMQP documentation](https://docs.solace.com/API/AMQP/AMQP-Getting-Started.htm).

## API scenario-by-scenario classroom reference

Run the relative path after the lesson base path. Expected responses below are assertions or specified outcomes; recorded excerpts are measured only for cases marked PASS. Never put an actual app key into the document.

| Case | Request / action | Expected | Recorded |
| --- | --- | --- | --- |
| API01_LIST | GET /trn/api/001/Products?$top=1 | 200 | PASS |
| API01_MULTIPLE | GET /trn/api/001/Products?$top=3 | 200 | PASS |
| API01_FILTER | GET /trn/api/001/Products?$filter=ProductID%20eq%201 | 200 | PASS |
| API01_UNKNOWN | GET /trn/api/001/NoSuchResource | 404 | PASS |
| API02_ONE | GET /trn/api/002/Products?$top=1 | 200 | PASS |
| API02_TWO | GET /trn/api/002/Products?$top=2 | 200 | PASS |
| API02_EMPTY | GET /trn/api/002/Products?$filter=ProductID%20eq%20-1 | 200 | PASS |
| API02_HEADER | GET /trn/api/002/Products?$top=1 | 200 | PASS |
| API03_MISSING | GET /trn/api/003/Products?$top=1 | 401 | PASS |
| API03_INVALID | GET /trn/api/003/Products?$top=1 | 401 | PASS |
| API03_VALID | GET /trn/api/003/Products?$top=1 | 200 | NOT_RUN |
| API03_REVOKED | GET /trn/api/003/Products?$top=1 | Revoke the dedicated training app key, then repeat; record the native rejection. Restore only the training app. | NOT_RUN |
| API04_FIRST_THREE | GET /trn/api/004/Products?$top=1 | [200, 200, 200] | PASS |
| API04_FOURTH | GET /trn/api/004/Products?$top=1 | [200, 200, 200, 429] | PASS |
| API04_SEPARATE | GET /trn/api/004/Products?$top=1 | 200 | PASS |
| API04_SPIKE | GET /trn/api/004/Products?$top=1 | Burst test uses X-Training-Mode: spike; distinguish TRAINING_SPIKE from TRAINING_QUOTA. | PASS |
| API05_VALID | GET /trn/api/005/Products?$top=2 | 200 | PASS |
| API05_ZERO | GET /trn/api/005/Products?$top=0 | 400 | PASS |
| API05_TEXT | GET /trn/api/005/Products?$top=abc | 400 | PASS |
| API05_LARGE | GET /trn/api/005/Products?$top=6 | 400 | PASS |
| API05_METHOD | POST /trn/api/005/Products | 405 | PASS |

### API01_LIST

Lesson: TRN_API_001_ProxyBasics. Request: `GET /trn/api/001/Products?$top=1`.

Expected configuration/checks:

```json
{
  "status": 200,
  "valueCount": 1
}
```
Recorded response excerpt (abbreviated for readability):

```text
{"@odata.context":"https://services.odata.org/V4/Northwind/Northwind.svc/$metadata#Products","value":[{"@odata.etag":"W/\"1,1\"","ProductID":1,"ProductName":"Chai","SupplierID":1,"CategoryID":1,"QuantityPerUnit":"10 boxes x 20 bags","UnitPrice":18.0000,"UnitsInStock":39,"UnitsOnOrder":0,"ReorderLevel":10,"Discontinued":false}]}
```

### API01_MULTIPLE

Lesson: TRN_API_001_ProxyBasics. Request: `GET /trn/api/001/Products?$top=3`.

Expected configuration/checks:

```json
{
  "status": 200,
  "valueCount": 3
}
```
Recorded response excerpt (abbreviated for readability):

```text
{"@odata.context":"https://services.odata.org/V4/Northwind/Northwind.svc/$metadata#Products","value":[{"@odata.etag":"W/\"1,1\"","ProductID":1,"ProductName":"Chai","SupplierID":1,"CategoryID":1,"QuantityPerUnit":"10 boxes x 20 bags","UnitPrice":18.0000,"UnitsInStock":39,"UnitsOnOrder":0,"ReorderLevel":10,"Discontinued":false},{"@odata.etag":"W/\"1,1\"","ProductID":2,"ProductName":"Chang","SupplierID":1,"CategoryID":1,"QuantityPerUnit":"24 - 12 oz bottles","UnitPrice":19.0000,"UnitsInStock":17,"UnitsOnOrder":40,"ReorderLevel":25,"Discontinued":false},{"@odata.etag":"W/\"1,2\"","ProductID":3,"ProductName":"Aniseed Syrup","SupplierID":1,"Categor …
```

### API01_FILTER

Lesson: TRN_API_001_ProxyBasics. Request: `GET /trn/api/001/Products?$filter=ProductID%20eq%201`.

Expected configuration/checks:

```json
{
  "status": 200,
  "productId": 1
}
```
Recorded response excerpt (abbreviated for readability):

```text
{"@odata.context":"https://services.odata.org/V4/Northwind/Northwind.svc/$metadata#Products","value":[{"@odata.etag":"W/\"1,1\"","ProductID":1,"ProductName":"Chai","SupplierID":1,"CategoryID":1,"QuantityPerUnit":"10 boxes x 20 bags","UnitPrice":18.0000,"UnitsInStock":39,"UnitsOnOrder":0,"ReorderLevel":10,"Discontinued":false}]}
```

### API01_UNKNOWN

Lesson: TRN_API_001_ProxyBasics. Request: `GET /trn/api/001/NoSuchResource`.

Expected configuration/checks:

```json
{
  "status": 404
}
```
Recorded response excerpt (abbreviated for readability):

```text
{"error":{"code":"","message":"Resource not found for the segment 'NoSuchResource'."}}
```

### API02_ONE

Lesson: TRN_API_002_Transform. Request: `GET /trn/api/002/Products?$top=1`.

Expected configuration/checks:

```json
{
  "status": 200,
  "count": 1
}
```
Recorded response excerpt (abbreviated for readability):

```text
{"count":1,"products":[{"id":1,"name":"Chai","price":18}]}
```

### API02_TWO

Lesson: TRN_API_002_Transform. Request: `GET /trn/api/002/Products?$top=2`.

Expected configuration/checks:

```json
{
  "status": 200,
  "count": 2
}
```
Recorded response excerpt (abbreviated for readability):

```text
{"count":2,"products":[{"id":1,"name":"Chai","price":18},{"id":2,"name":"Chang","price":19}]}
```

### API02_EMPTY

Lesson: TRN_API_002_Transform. Request: `GET /trn/api/002/Products?$filter=ProductID%20eq%20-1`.

Expected configuration/checks:

```json
{
  "status": 200,
  "count": 0
}
```
Recorded response excerpt (abbreviated for readability):

```text
{"count":0,"products":[]}
```

### API02_HEADER

Lesson: TRN_API_002_Transform. Request: `GET /trn/api/002/Products?$top=1`.

Expected configuration/checks:

```json
{
  "status": 200,
  "header": "X-Training-Course",
  "headerValue": "CPI-API-Training"
}
```
Recorded response excerpt (abbreviated for readability):

```text
{"count":1,"products":[{"id":1,"name":"Chai","price":18}]}
```

### API03_MISSING

Lesson: TRN_API_003_APIKey. Request: `GET /trn/api/003/Products?$top=1`.

Expected configuration/checks:

```json
{
  "status": 401
}
```
Recorded response excerpt (abbreviated for readability):

```text
{"fault":{"faultstring":"Failed to resolve API Key variable request.header.X-API-Key","detail":{"errorcode":"steps.oauth.v2.FailedToResolveAPIKey"}}}
```

### API03_INVALID

Lesson: TRN_API_003_APIKey. Request: `GET /trn/api/003/Products?$top=1`.

Expected configuration/checks:

```json
{
  "status": 401,
  "apiKey": "invalid-training-key"
}
```
Recorded response excerpt (abbreviated for readability):

```text
{"fault":{"faultstring":"Invalid ApiKey","detail":{"errorcode":"oauth.v2.InvalidApiKey"}}}
```

### API03_VALID

Lesson: TRN_API_003_APIKey. Request: `GET /trn/api/003/Products?$top=1`.

Expected configuration/checks:

```json
{
  "status": 200,
  "keyFromEnvironment": true
}
```
User asked to skip further testing. Requires a dedicated Developer Hub application/key and the documented lifecycle action.


### API03_REVOKED

Lesson: TRN_API_003_APIKey. Request: `GET /trn/api/003/Products?$top=1`.

Expected configuration/checks:

```json
{
  "manual": "Revoke the dedicated training app key, then repeat; record the native rejection. Restore only the training app."
}
```
User asked to skip further testing. Requires a dedicated Developer Hub application/key and the documented lifecycle action.


### API04_FIRST_THREE

Lesson: TRN_API_004_Traffic. Request: `GET /trn/api/004/Products?$top=1`.

Expected configuration/checks:

```json
{
  "sequenceStatuses": [
    200,
    200,
    200
  ],
  "freshStudent": true
}
```
Recorded sequential statuses: [200, 200, 200]. Each sequence uses a fresh training identifier.


### API04_FOURTH

Lesson: TRN_API_004_Traffic. Request: `GET /trn/api/004/Products?$top=1`.

Expected configuration/checks:

```json
{
  "sequenceStatuses": [
    200,
    200,
    200,
    429
  ],
  "freshStudent": true
}
```
Recorded sequential statuses: [200, 200, 200, 429]. Each sequence uses a fresh training identifier.


### API04_SEPARATE

Lesson: TRN_API_004_Traffic. Request: `GET /trn/api/004/Products?$top=1`.

Expected configuration/checks:

```json
{
  "objective": "A fresh student identifier has an independent quota",
  "status": 200,
  "freshStudent": true
}
```
Recorded response excerpt (abbreviated for readability):

```text
{"@odata.context":"https://services.odata.org/V4/Northwind/Northwind.svc/$metadata#Products","value":[{"@odata.etag":"W/\"1,1\"","ProductID":1,"ProductName":"Chai","SupplierID":1,"CategoryID":1,"QuantityPerUnit":"10 boxes x 20 bags","UnitPrice":18.0000,"UnitsInStock":39,"UnitsOnOrder":0,"ReorderLevel":10,"Discontinued":false}]}
```

### API04_SPIKE

Lesson: TRN_API_004_Traffic. Request: `GET /trn/api/004/Products?$top=1`.

Expected configuration/checks:

```json
{
  "objective": "Send a controlled concurrent burst and observe rate rejection; exact acceptance count depends on scheduling",
  "manual": "Burst test uses X-Training-Mode: spike; distinguish TRAINING_SPIKE from TRAINING_QUOTA."
}
```
Recorded controlled burst: 13 accepted; 2 rejected with TRAINING_SPIKE. Count is timing-dependent.


### API05_VALID

Lesson: TRN_API_005_ValidationFaults. Request: `GET /trn/api/005/Products?$top=2`.

Expected configuration/checks:

```json
{
  "status": 200
}
```
Recorded response excerpt (abbreviated for readability):

```text
{"@odata.context":"https://services.odata.org/V4/Northwind/Northwind.svc/$metadata#Products","value":[{"@odata.etag":"W/\"1,1\"","ProductID":1,"ProductName":"Chai","SupplierID":1,"CategoryID":1,"QuantityPerUnit":"10 boxes x 20 bags","UnitPrice":18.0000,"UnitsInStock":39,"UnitsOnOrder":0,"ReorderLevel":10,"Discontinued":false},{"@odata.etag":"W/\"1,1\"","ProductID":2,"ProductName":"Chang","SupplierID":1,"CategoryID":1,"QuantityPerUnit":"24 - 12 oz bottles","UnitPrice":19.0000,"UnitsInStock":17,"UnitsOnOrder":40,"ReorderLevel":25,"Discontinued":false}]}
```

### API05_ZERO

Lesson: TRN_API_005_ValidationFaults. Request: `GET /trn/api/005/Products?$top=0`.

Expected configuration/checks:

```json
{
  "status": 400,
  "errorCode": "INVALID_TOP"
}
```
Recorded response excerpt (abbreviated for readability):

```text
{"error": {"code": "INVALID_TOP", "message": "Use top from 1 to 5"}}
```

### API05_TEXT

Lesson: TRN_API_005_ValidationFaults. Request: `GET /trn/api/005/Products?$top=abc`.

Expected configuration/checks:

```json
{
  "status": 400,
  "errorCode": "INVALID_TOP"
}
```
Recorded response excerpt (abbreviated for readability):

```text
{"error": {"code": "INVALID_TOP", "message": "Use top from 1 to 5"}}
```

### API05_LARGE

Lesson: TRN_API_005_ValidationFaults. Request: `GET /trn/api/005/Products?$top=6`.

Expected configuration/checks:

```json
{
  "status": 400,
  "errorCode": "INVALID_TOP"
}
```
Recorded response excerpt (abbreviated for readability):

```text
{"error": {"code": "INVALID_TOP", "message": "Use top from 1 to 5"}}
```

### API05_METHOD

Lesson: TRN_API_005_ValidationFaults. Request: `POST /trn/api/005/Products`.

Expected configuration/checks:

```json
{
  "method": "POST",
  "status": 405,
  "errorCode": "METHOD_NOT_ALLOWED"
}
```
Recorded response excerpt (abbreviated for readability):

```text
{"error": {"code": "METHOD_NOT_ALLOWED", "message": "Only GET is allowed"}}
```