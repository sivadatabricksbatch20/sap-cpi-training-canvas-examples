// AMQP wire-protocol fixture only. It is not a Solace/SAP broker.
const rhea=require('rhea');
const assert=require('node:assert/strict');
const {spawn}=require('node:child_process');
const net=require('node:net');
const path=require('node:path');
async function port(){return new Promise(resolve=>{const s=net.createServer();s.listen(0,'127.0.0.1',()=>{const p=s.address().port;s.close(()=>resolve(p));});});}
async function test(action){
 const p=await port();const server=rhea.create_container();server.sasl_server_mechanisms.enable_plain(()=>true);const listener=server.listen({host:'127.0.0.1',port:p});let delivered=false;
 server.on('message',c=>{assert.equal(JSON.parse(String(c.message.body)).data.orderId,'SO1001');delivered=true;c.delivery.accept();});
 server.on('sendable',c=>{if(action==='consume'&&!delivered){delivered=true;c.sender.send({message_id:'fixture-1',body:JSON.stringify({data:{orderId:'SO1001'}})});}});
 await new Promise((resolve,reject)=>{
  const child=spawn(process.execPath,[path.join(__dirname,'client.cjs'),action,action==='publish'?'topic://training/orders/created/v1':'training.q.orders',...(action==='publish'?[path.join(__dirname,'../event-mesh/order-created.json')]:[])],{env:{...process.env,AEM_HOST:'127.0.0.1',AEM_PORT:String(p),AEM_USERNAME:'fixture-client',AEM_PASSWORD:'fixture-password',AEM_LOCAL_PLAINTEXT:'true'}});let output='';child.stdout.on('data',b=>output+=b);child.stderr.on('data',b=>output+=b);child.on('close',code=>{try{assert.equal(code,0,output);assert.ok(output.includes('"status":"PASS"'));assert.ok(delivered);resolve();}catch(e){reject(e);}});
 });listener.close();
}
(async()=>{await test('publish');await test('consume');console.log(JSON.stringify({method:'LOCAL_AMQP_PROTOCOL_FIXTURE',passed:2,note:'Publish acceptance and manual consumption acknowledgement; not SAP/Solace product validation.'}));})().catch(e=>{console.error(e);process.exit(1);});
