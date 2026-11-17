var top=context.getVariable('request.queryparam.$top');
context.setVariable('training.invalidTop',top!==null && top!==undefined && !/^[1-5]$/.test(String(top)));
