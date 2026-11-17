"""HTTP contract test only: this fixture is not a SAP broker."""
import base64,json,threading,unittest
from http.server import BaseHTTPRequestHandler,ThreadingHTTPServer
from client import EventMesh
class Fixture(BaseHTTPRequestHandler):
 requests=[]
 def log_message(self,*args):pass
 def do_POST(self):
  body=self.rfile.read(int(self.headers.get('Content-Length','0')));self.requests.append((self.path,self.headers,body))
  if self.path=='/oauth/token':
   assert self.headers['Authorization']=='Basic '+base64.b64encode(b'unit-client:unit-secret').decode();self.send_response(200);self.end_headers();self.wfile.write(b'{"access_token":"fixture-token","expires_in":3600}');return
  assert self.headers['Authorization']=='Bearer fixture-token'
  if self.path.endswith('/consumption'):
   self.send_response(200);self.send_header('X-Message-Id','ID:1001/fixture');self.end_headers();self.wfile.write(b'{"orderId":"SO1001"}')
  else:self.send_response(202);self.end_headers()
class Contract(unittest.TestCase):
 def test_publish_consume_ack_and_token_reuse(self):
  server=ThreadingHTTPServer(('127.0.0.1',0),Fixture);thread=threading.Thread(target=server.serve_forever,daemon=True);thread.start();base='http://127.0.0.1:'+str(server.server_port)
  try:
   mesh=EventMesh({'messaging':[{'protocol':['httprest'],'uri':base,'oa2':{'clientid':'unit-client','clientsecret':'unit-secret','tokenendpoint':base+'/oauth/token'}}]})
   self.assertEqual(mesh.publish('training/orders/created',{'orderId':'SO1001'})[0],202)
   status,headers,body=mesh.consume('training/q.orders');self.assertEqual(status,200);self.assertEqual(json.loads(body)['orderId'],'SO1001');mesh.acknowledge('training/q.orders',headers['X-Message-Id'])
   paths=[r[0] for r in Fixture.requests];self.assertEqual(paths,['/oauth/token','/messagingrest/v1/topics/training%2Forders%2Fcreated/messages','/messagingrest/v1/queues/training%2Fq.orders/messages/consumption','/messagingrest/v1/queues/training%2Fq.orders/messages/ID%3A1001%2Ffixture/acknowledgement']);self.assertEqual(Fixture.requests[1][1]['x-qos'],'1')
  finally:server.shutdown();server.server_close();thread.join()
 def test_missing_binding_fails_before_network(self):
  with self.assertRaisesRegex(ValueError,'httprest'):EventMesh({'messaging':[]})
if __name__=='__main__':unittest.main()
