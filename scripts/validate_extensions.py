"""Validate native API archives and distinguish broker prerequisites from live proof."""
from pathlib import Path,PurePosixPath
import json,hashlib,zipfile,xml.etree.ElementTree as ET
ROOT=Path(__file__).resolve().parents[1];api=ROOT/'api-management';catalog=json.loads((api/'catalogue.json').read_text());manifest=json.loads((api/'manifest.json').read_text());rows={r['name']:r for r in manifest['proxies']}
assert len(catalog)==len(rows)==5
assert sum(len(x['cases']) for x in catalog)==21
ids=set()
for lab in catalog:
 row=rows[lab['id']];assert row['state']=='DEPLOYED';folder=api/lab['id'];archive=folder/(lab['id']+'.zip');assert hashlib.sha256(archive.read_bytes()).hexdigest()==row['archiveSha256']
 with zipfile.ZipFile(archive) as z:
  for name in z.namelist():
   path=PurePosixPath(name);assert not path.is_absolute() and '..' not in path.parts
   if name.endswith('/'):continue
   assert (folder/name).read_bytes()==z.read(name)
   if name.endswith('.xml'):ET.fromstring(z.read(name))
 root=ET.parse(folder/'APIProxy'/(lab['id']+'.xml')).getroot();local=lambda tag:tag.rsplit('}',1)[-1]
 declared={e.text for e in root.iter() if local(e.tag)=='policy'}
 for endpoint in (folder/'APIProxy/APIProxyEndPoint').glob('*.xml'):
  tree=ET.parse(endpoint)
  assert all(e.text in declared for e in tree.iter() if local(e.tag)=='policy_name')
 for case in lab['cases']:
  assert case['id'] not in ids;ids.add(case['id'])
evidence=json.loads((ROOT/'tests/evidence/api-management-results.json').read_text())
assert {r['scenarioId'] for r in evidence['results']}==ids
assert all(r['verdict'] in ['PASS','NOT_RUN'] for r in evidence['results'])
assert all(r.get('reason') for r in evidence['results'] if r['verdict']=='NOT_RUN')
for product in ['event-mesh','advanced-event-mesh']:
 labs=json.loads((ROOT/product/'catalogue.json').read_text());assert len(labs)==5
 assert all(x['readiness'].startswith('BLOCKED') for x in labs)
 assert all('fixture' in x['verification'].lower() or 'contract' in x['verification'].lower() for x in labs)
print('PASS: 5 native API archives/revisions, 21 API cases, policy links, source checksums, 10 explicitly prerequisite-blocked mesh exercises.')
