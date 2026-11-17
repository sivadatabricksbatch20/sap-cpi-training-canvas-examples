var response = JSON.parse(context.getVariable('response.content'));
if (!Array.isArray(response.value)) throw new Error('Expected OData value array');
var products = response.value.map(function(p) { return {id:p.ProductID,name:p.ProductName,price:p.UnitPrice}; });
context.setVariable('response.content',JSON.stringify({count:products.length,products:products}));
context.setVariable('response.header.Content-Type','application/json; charset=utf-8');
