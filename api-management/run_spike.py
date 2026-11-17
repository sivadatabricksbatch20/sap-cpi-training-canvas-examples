import urllib.request,urllib.error,json,uuid,concurrent.futures,argparse,os
from pathlib import Path
parser=argparse.ArgumentParser();parser.add_argument('--base-url',default=os.environ.get('APIM_BASE_URL'));parser.add_argument('--output',default='/tmp/apim-spike-results.json');args=parser.parse_args()
if not args.base_url:parser.error('Set APIM_BASE_URL or pass --base-url')
base=args.base_url.rstrip('/');student='spike-'+uuid.uuid4().hex
url=base+'/trn/api/004/Products?$top=1'
def run(i):
 req=urllib.request.Request(url,headers={'Accept':'application/json','X-Training-Student':student,'X-Training-Mode':'spike'})
 try:
  try:r=urllib.request.urlopen(req,timeout=30)
  except urllib.error.HTTPError as e:r=e
  with r:status=r.status;body=r.read().decode()
  try:data=json.loads(body)
  except ValueError:data={}
  code=data.get('error',{}).get('code')
  return {'request':i,'status':status,'errorCode':code,'proxyMissing':data.get('fault',{}).get('detail',{}).get('errorcode')=='messaging.adaptors.http.flow.ApplicationNotFound'}
 except Exception as e:return {'request':i,'error':str(e)}
with concurrent.futures.ThreadPoolExecutor(max_workers=10) as pool:results=list(pool.map(run,range(15)))
good=any(r.get('status')==429 and r.get('errorCode')=='TRAINING_SPIKE' for r in results) and any(r.get('status')==200 for r in results) and not any(r.get('proxyMissing') or r.get('error') or r.get('status') not in [200,429] for r in results)
out={'scenarioId':'API04_SPIKE','method':'LIVE_SAP_API_MANAGEMENT','verdict':'PASS' if good else 'FAIL','results':results,'note':'A controlled 15-request classroom burst; acceptance count depends on scheduling.'};Path(args.output).write_text(json.dumps(out,indent=2));print('Spike result',out['verdict'],{c:sum(r.get('status')==c for r in results) for c in [200,429]})
