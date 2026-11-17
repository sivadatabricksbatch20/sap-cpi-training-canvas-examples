"""Validate the versioned CPI source snapshot without tenant credentials."""
from pathlib import Path
import json,re,sys,xml.etree.ElementTree as ET,zipfile,io,base64
ROOT=Path(__file__).resolve().parents[1]
errors=[];xml_count=0
manifest=json.loads((ROOT/'manifest.json').read_text())['artifacts']
if len(manifest)!=64:errors.append('Expected 64 iFlows')
ids=set()
secret=re.compile(r'-----BEGIN (?:[A-Z ]*PRIVATE KEY|PGP PRIVATE KEY BLOCK)-----|(?:gh[pousr]_[A-Za-z0-9]{30,}|github_pat_[A-Za-z0-9_]{30,})|JSESSIONID=[A-Za-z0-9]{10,}')
def scan(name,data):
 if zipfile.is_zipfile(io.BytesIO(data)):
  with zipfile.ZipFile(io.BytesIO(data)) as z:
   for item in z.infolist():
    if item.is_dir():continue
    scan(name+'!'+item.filename,z.read(item))
  return
 if name.endswith('resources.cnt'):
  try:data=base64.b64decode(data)
  except Exception:pass
 try:text=data.decode('utf-8')
 except UnicodeDecodeError:return
 if secret.search(text):errors.append('Possible credential/private key: '+name)
for row in manifest:
 fid=row['id'];ids.add(fid);folder=ROOT/row['directory'];files=list(folder.glob('src/main/resources/scenarioflows/integrationflow/*.iflw'))
 if len(files)!=1:errors.append(fid+': expected one .iflw');continue
 mf=(folder/'META-INF/MANIFEST.MF').read_text();unfold=mf.replace('\n ','')
 if not re.search(r'^Bundle-SymbolicName: '+re.escape(fid)+r'(?:;|\s|$)',unfold,re.M):errors.append(fid+': symbolic name mismatch')
 if not re.search(r'^Bundle-Version: '+re.escape(row['version'])+r'\s*$',unfold,re.M):errors.append(fid+': version mismatch')
 for path in folder.rglob('*'):
  if not path.is_file():continue
  if path.suffix in ['.iflw','.xsd','.xslt','.xsl','.mmap','.propdef'] or path.name=='.project':
   try:ET.parse(path);xml_count+=1
   except ET.ParseError as e:errors.append(str(path.relative_to(ROOT))+': '+str(e))
 if int(fid.split('_')[2]) in [2,3,4,5,6]:
  for script in ['LoadScenario.groovy','AssertScenario.groovy']:
   if not (folder/'src/main/resources/script'/script).is_file():errors.append(fid+': missing '+script)
  prop=(folder/'src/main/resources/parameters.prop').read_text()
  if not re.search(r'^ScenarioId=',prop,re.M):errors.append(fid+': ScenarioId default missing')
if len(ids)!=64:errors.append('Duplicate flow IDs')
cases=json.loads((ROOT/'tests/scenario-catalog.json').read_text());results=json.loads((ROOT/'tests/evidence/pilot-results.json').read_text())
if sum(len(x['cases']) for x in cases)!=49 or len(results)!=49:errors.append('Pilot must contain 49 scenario definitions and results')
for result in results:
 status=(result.get('assertionReport') or {}).get('status')
 if not result['expectedRejection'] and status not in ['PASS','OBSERVED']:errors.append('Unexpected scenario outcome: '+result['scenarioId'])
for path in ROOT.rglob('*'):
 if not path.is_file() or '.git' in path.relative_to(ROOT).parts:continue
 if path.name.endswith(('session.json','csrf.json')):errors.append('Session file included: '+str(path.relative_to(ROOT)))
 scan(str(path.relative_to(ROOT)),path.read_bytes())
if errors:
 print('\n'.join(errors));sys.exit(1)
print(f'PASS: {len(ids)} iFlows; {xml_count} XML resources; 49 scenario records; parameter defaults; recursive export/document credential scan.')
