const fs=require('node:fs');
const vm=require('node:vm');
const assert=require('node:assert/strict');
const path=require('node:path');
const root=__dirname;
function run(file,vars){const context={getVariable:k=>Object.hasOwn(vars,k)?vars[k]:null,setVariable:(k,v)=>{vars[k]=v}};vm.runInNewContext(fs.readFileSync(path.join(root,file),'utf8'),{context});return vars;}
let tested=0;
for(const size of [0,1,3]){
 const value=Array.from({length:size},(_,i)=>({ProductID:i+1,ProductName:'Product '+i,UnitPrice:10+i,InternalField:'remove'}));
 const vars=run('TRN_API_002_Transform/APIProxy/FileResource/TransformProducts.js',{'response.content':JSON.stringify({value})});
 const out=JSON.parse(vars['response.content']);assert.equal(out.count,size);assert.deepEqual(out.products.map(p=>Object.keys(p)),Array.from({length:size},()=>['id','name','price']));tested++;
}
for(const [input,invalid] of [[null,false],['1',false],['5',false],['0',true],['6',true],['abc',true],['1;DROP',true],['',true]]){
 const vars=run('TRN_API_005_ValidationFaults/APIProxy/FileResource/ValidateTop.js',{'request.queryparam.$top':input});assert.equal(vars['training.invalidTop'],invalid);tested++;
}
console.log(JSON.stringify({method:'LOCAL_JAVASCRIPT_UNIT_TEST',passed:tested,note:'Checks script logic only; not native SAP policy enforcement.'}));
