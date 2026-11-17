'use strict';
const fs=require('node:fs');
const crypto=require('node:crypto');
const rhea=require('rhea');
const [action,address,file]=process.argv.slice(2);
if(!['publish','consume','release-once'].includes(action)||!address){console.error('Usage: node client.cjs publish <broker-topic-address> <event.json> | consume <broker-queue-address> | release-once <broker-queue-address>');process.exit(2);}
for(const key of ['AEM_HOST','AEM_USERNAME','AEM_PASSWORD'])if(!process.env[key])throw new Error('Set '+key+' in your shell');
const plaintext=process.env.AEM_LOCAL_PLAINTEXT==='true';
if(plaintext&&!['localhost','127.0.0.1'].includes(process.env.AEM_HOST))throw new Error('Plaintext is allowed only for the local broker lab');
const options={host:process.env.AEM_HOST,port:Number(process.env.AEM_PORT||(plaintext?'15672':'5671')),transport:plaintext?'tcp':'tls',username:process.env.AEM_USERNAME,password:process.env.AEM_PASSWORD,reconnect:false};
if(!plaintext)options.rejectUnauthorized=true;
const client=rhea.create_container({autoaccept:false,client_id:'training-'+crypto.randomUUID()});
const connection=client.connect(options);let sent=false,settled=false,released=false;
const timeout=setTimeout(()=>finish(new Error('Timed out waiting for broker delivery/settlement')),30000);
function finish(error){if(settled)return;settled=true;clearTimeout(timeout);connection.close();if(error){console.error(JSON.stringify({status:'FAILED',message:error.message}));process.exitCode=1;}else console.log(JSON.stringify({status:'PASS',method:'AMQP_BROKER',action}));}
client.on('connection_open',()=>action==='publish'?connection.open_sender(address):connection.open_receiver({source:{address},autoaccept:false,credit_window:1}));
client.on('sendable',c=>{if(action!=='publish'||sent)return;sent=true;const payload=JSON.parse(fs.readFileSync(file,'utf8'));c.sender.send({message_id:payload.id||crypto.randomUUID(),durable:true,content_type:'application/json',body:JSON.stringify(payload)});});
client.on('accepted',()=>{if(action==='publish')finish();});
client.on('message',c=>{let event;try{event=JSON.parse(String(c.message.body));}catch(error){c.delivery.reject({condition:'amqp:decode-error',description:'Training fixture must be JSON'});finish(error);return;}
 console.log(JSON.stringify({receivedId:c.message.message_id,event}));
 if(action==='release-once'&&!released){released=true;c.delivery.release();console.log(JSON.stringify({action:'RELEASED_FIRST_DELIVERY',expected:'broker redelivery; no business commit was performed'}));return;}
 c.delivery.accept();finish();});
for(const event of ['connection_error','sender_error','receiver_error'])client.on(event,c=>finish(new Error(String(c.error||c.connection?.error||c.sender?.error||c.receiver?.error||event))));
client.on('rejected',()=>finish(new Error('Broker rejected the outgoing message')));
client.on('disconnected',()=>{if(!settled)finish(new Error('Connection ended before the expected outcome'));});
