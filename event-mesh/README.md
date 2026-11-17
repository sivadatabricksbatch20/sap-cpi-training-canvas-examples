# SAP Event Mesh exercises

Five documented exercises: publish/consume/acknowledge, fan-out, competing consumers, offline durability/idempotency, and retry/dead-letter handling. See [the guide](../docs/API_MANAGEMENT_AND_MESH_GUIDE.md) and `catalogue.json`.

**Cloud prerequisite blocked:** this trial has no SAP Event Mesh instance or available service-plan assignment. The REST client and local HTTP contract tests are provided; native broker behaviour has not been verified.

```bash
export EM_SERVICE_KEY_FILE='/secure/local/path/service-key.json'
python event-mesh/client.py publish '<namespace>/orders/created/v1' --event event-mesh/order-created.json
python event-mesh/client.py consume '<namespace>/q.orders'
python event-mesh/client.py ack '<namespace>/q.orders' --message-id 'MESSAGE-ID-FROM-CONSUME'
python event-mesh/test_client.py
```

Create the dedicated queue and topic subscription in your provisioned service first. Consume/ack act on real messages. Test credentials in the HTTP fixture are synthetic; its results establish request construction and token reuse only.
