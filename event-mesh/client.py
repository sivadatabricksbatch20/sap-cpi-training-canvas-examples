"""SAP Event Mesh REST lab client. Credentials stay in a local service-key file."""
import argparse,base64,json,os,time,urllib.parse,urllib.request
from pathlib import Path
class EventMesh:
 def __init__(self,key):
  bindings=[x for x in key.get('messaging',[]) if 'httprest' in x.get('protocol',[])]
  if len(bindings)!=1:raise ValueError('Expected exactly one messaging binding with protocol httprest')
  self.binding=bindings[0];self.token=None;self.expires=0
 def access_token(self):
  if self.token and time.monotonic()<self.expires:return self.token
  auth=self.binding['oa2'];basic=base64.b64encode((auth['clientid']+':'+auth['clientsecret']).encode()).decode()
  req=urllib.request.Request(auth['tokenendpoint'],data=b'grant_type=client_credentials',headers={'Authorization':'Basic '+basic,'Content-Type':'application/x-www-form-urlencoded'},method='POST')
  with urllib.request.urlopen(req,timeout=30) as response:data=json.load(response)
  self.token=data['access_token'];self.expires=time.monotonic()+max(0,int(data.get('expires_in',60))-30);return self.token
 def call(self,path,data=b'',qos=False):
  headers={'Authorization':'Bearer '+self.access_token(),'Content-Type':'application/json'}
  if qos:headers['x-qos']='1'
  req=urllib.request.Request(self.binding['uri'].rstrip('/')+'/messagingrest/v1/'+path,data=data,headers=headers,method='POST')
  with urllib.request.urlopen(req,timeout=30) as response:return response.status,dict(response.headers),response.read()
 def publish(self,topic,event):return self.call('topics/'+urllib.parse.quote(topic,safe='')+'/messages',json.dumps(event,ensure_ascii=False).encode(),True)
 def consume(self,queue):return self.call('queues/'+urllib.parse.quote(queue,safe='')+'/messages/consumption',qos=True)
 def acknowledge(self,queue,message_id):return self.call('queues/'+urllib.parse.quote(queue,safe='')+'/messages/'+urllib.parse.quote(message_id,safe='')+'/acknowledgement')
if __name__=='__main__':
 p=argparse.ArgumentParser();p.add_argument('action',choices=['publish','consume','ack']);p.add_argument('address');p.add_argument('--event');p.add_argument('--message-id');args=p.parse_args()
 key=Path(os.environ['EM_SERVICE_KEY_FILE']);client=EventMesh(json.loads(key.read_text()))
 if args.action=='publish':
  if not args.event:p.error('publish requires --event')
  status,headers,body=client.publish(args.address,json.loads(Path(args.event).read_text()))
 elif args.action=='consume':status,headers,body=client.consume(args.address)
 else:
  if not args.message_id:p.error('ack requires --message-id')
  status,headers,body=client.acknowledge(args.address,args.message_id)
 print(json.dumps({'status':status,'messageId':next((v for k,v in headers.items() if k.lower()=='x-message-id'),None),'body':body.decode('utf-8',errors='replace')},ensure_ascii=False))
