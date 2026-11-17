# Advanced event mesh exercises

Five documented exercises: durable topic-to-queue delivery, wildcards/fan-out, competing consumers, release/redelivery/DMQ, and TTL with advanced-design extensions. See [the guide](../docs/API_MANAGEMENT_AND_MESH_GUIDE.md) and `catalogue.json`.

**Cloud prerequisite blocked:** advanced event mesh is absent from the trial’s available service assignments. The optional official local Solace broker could not start in this managed workspace because a required kernel interface is unavailable. No SAP/Solace broker pass is claimed.

```bash
cd advanced-event-mesh
npm ci --ignore-scripts
# Set AEM_HOST, AEM_PORT, AEM_USERNAME and AEM_PASSWORD privately.
node client.cjs publish 'BROKER-TOPIC-ADDRESS' ../event-mesh/order-created.json
node client.cjs consume 'BROKER-QUEUE-ADDRESS'
node client.cjs release-once 'BROKER-QUEUE-ADDRESS'
node test_amqp.cjs
```

Use the address format and full username supplied by the broker. Configure durable queues/subscriptions and a non-exclusive queue for competing consumers. Set redelivery/DMQ behaviour in the broker manager. Cloud connections verify TLS certificates.

The AMQP fixture tests publisher acceptance and manual receiver acknowledgement over the wire, but does not implement a SAP/Solace broker. `compose.yaml` provides an optional pinned official local broker for a supported Docker host; it binds ports to loopback and requires a private admin password.
