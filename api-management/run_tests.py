"""Exercise deployed native API proxies. Does not emulate SAP policies."""
import argparse,json,os,time,urllib.request,urllib.error,uuid
from pathlib import Path
R=Path(__file__).parent
p=argparse.ArgumentParser();p.add_argument('--base-url',default=os.environ.get('APIM_BASE_URL'));p.add_argument('--lesson',type=int);p.add_argument('--output',default='live-results.json');args=p.parse_args()
if not args.base_url:p.error('Set APIM_BASE_URL to the deployed gateway host, or pass --base-url')
def request(lab,case,student=None):
 headers={'Accept':'application/json'}
 if student:headers['X-Training-Student']=student
 if case.get('apiKey'):headers['X-API-Key']=case['apiKey']
 if case.get('keyFromEnvironment'):
  if not os.environ.get('APIM_TRAINING_API_KEY'):return {'scenarioId':case['id'],'verdict':'BLOCKED','reason':'Set APIM_TRAINING_API_KEY from the dedicated training app'}
  headers['X-API-Key']=os.environ['APIM_TRAINING_API_KEY']
 req=urllib.request.Request(args.base_url.rstrip('/')+lab['basePath']+case['path'],headers=headers,method=case.get('method','GET'))
 try:
  try:response=urllib.request.urlopen(req,timeout=45)
  except urllib.error.HTTPError as error:response=error
  with response:status=response.status;body=response.read().decode('utf-8',errors='replace');response_headers=dict(response.headers)
  try:data=json.loads(body)
  except ValueError:data={}
  good=status==case.get('status',200) and data.get('fault',{}).get('detail',{}).get('errorcode')!='messaging.adaptors.http.flow.ApplicationNotFound'
  if 'valueCount' in case:good=good and len(data.get('value',[]))==case['valueCount']
  if 'count' in case:good=good and data.get('count')==case['count']
  if 'productId' in case:good=good and bool(data.get('value')) and data['value'][0].get('ProductID')==case['productId']
  if case.get('errorCode'):good=good and data.get('error',{}).get('code')==case['errorCode']
  if case.get('header'):good=good and next((v for k,v in response_headers.items() if k.lower()==case['header'].lower()),None)==case['headerValue']
  return {'scenarioId':case['id'],'status':status,'verdict':'PASS' if good else 'FAIL','body':body[:2000]}
 except Exception as error:return {'scenarioId':case['id'],'verdict':'ERROR','error':str(error)}
results=[]
for lab in json.loads((R/'catalogue.json').read_text()):
 if args.lesson and lab['number']!=args.lesson:continue
 for case in lab['cases']:
  if case.get('manual'):results.append({'scenarioId':case['id'],'verdict':'MANUAL','reason':case['manual']});continue
  student='student-'+uuid.uuid4().hex
  if case.get('sequenceStatuses'):
   sequence=[]
   for status in case['sequenceStatuses']:
    run=request(lab,dict(case,status=status),student);sequence.append(run);time.sleep(.2)
   result={'scenarioId':case['id'],'verdict':'PASS' if all(r['verdict']=='PASS' for r in sequence) else 'FAIL','sequence':sequence}
  else:result=request(lab,case,student)
  results.append(result);print(lab['id'],case['id'],result['verdict'],flush=True)
Path(args.output).write_text(json.dumps({'method':'LIVE_SAP_API_MANAGEMENT','baseUrl':args.base_url,'results':results},indent=2)+'\n')
if any(r['verdict'] in ['FAIL','ERROR'] for r in results):raise SystemExit(1)
