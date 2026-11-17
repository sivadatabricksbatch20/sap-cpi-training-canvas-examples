"""Import and deploy a catalogued Classic API Management proxy using an API-access token."""
import argparse,base64,json,os,urllib.parse,urllib.request
from pathlib import Path
R=Path(__file__).parent
p=argparse.ArgumentParser();p.add_argument('name',choices=[x['id'] for x in json.loads((R/'catalogue.json').read_text())]);args=p.parse_args()
base=os.environ['APIM_MANAGEMENT_BASE_URL'].rstrip('/');token=os.environ['APIM_MANAGEMENT_TOKEN']
if not base.startswith('https://'):p.error('APIM_MANAGEMENT_BASE_URL must use HTTPS and end with /apiportal/api/1.0')
archive=R/args.name/(args.name+'.zip')
headers={'Authorization':'Bearer '+token,'Accept':'application/json'}
def call(path,data,method,content_type):
 request=urllib.request.Request(base+path,data=data,method=method,headers=dict(headers,**{'Content-Type':content_type}))
 with urllib.request.urlopen(request,timeout=60) as response:return response.status,response.read()
status,_=call('/Transport.svc/APIProxies',base64.b64encode(archive.read_bytes()),'POST','application/octet-stream');print('Import accepted:',status)
status,_=call("/Management.svc/APIProxies('"+args.name+"')?mode=deploy",b'{"state":"DEPLOYED"}','PATCH','application/json');print('Deployment accepted:',status)
print('Verify the deployed URL with run_tests.py. Re-importing a proxy creates/replaces a native revision; acceptance is not a runtime test.')
