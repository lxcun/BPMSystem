function getOptions(key, detailList){
    var options = "";
    var itemMap = new Map();
    for(var i = 0; i < detailList.length; i ++)
    {
        if(!itemMap.has(detailList[i][key])){
            itemMap.set(detailList[i][key], 1);
            options += "<option>" + detailList[i][key] + "</option>";
        }
    }
    return options;
}

function setOptions(id, key, detailList){
    var options = "";
    var itemMap = new Map();
    for(var i = 0; i < detailList.length; i ++)
    {
        if(!itemMap.has(detailList[i][key])){
            itemMap.set(detailList[i][key], 1);
            options += "<option>" + detailList[i][key] + "</option>";
        }
    }
    $(id).html("");
    $(id).append(options);
}

//从结构列表中，找出某一个关键字的所有不重复值作为自身下拉列表的选项
function setDefaultOptions(id, key, detailList){
    var options = "";
    var itemMap = new Map();
    options += "<option></option>";
    if(detailList != null)
    {
        for(var i = 0; i < detailList.length; i ++)
        {
            if(!itemMap.has(detailList[i][key])){
                itemMap.set(detailList[i][key], 1);
                options += "<option>" + detailList[i][key] + "</option>";
            }
        }
    }
    $(id).html("");
    $(id).append(options);
}

//从结构列表中，过滤出匹配值的下拉列表的选项
function setDefaultMatchOptions(id, key, detailList, matchName, matchValue){
    var options = "";
    var itemMap = new Map();
    options += "<option>" + "-请选择-" + "</option>";
    if(detailList != null)
    {
        for(var i = 0; i < detailList.length; i ++)
        {
            if(detailList[i][matchName] == matchValue){
                if(!itemMap.has(detailList[i][key])){
                    itemMap.set(detailList[i][key], 1);
                    options += "<option>" + detailList[i][key] + "</option>";
                }
            }
        }
    }
    $(id).html("");
    $(id).append(options);
}

//从结构列表中，过滤出匹配值的下拉列表的选项
function setNoSavedMatchOptions(id, key, detailList, matchName, matchValue, savedMap){
    var options = "";
    var itemMap = new Map();
    options += "<option></option>";
    if(detailList != null)
    {
        for(var i = 0; i < detailList.length; i ++)
        {
            if(detailList[i][matchName] == matchValue){
                if((!itemMap.has(detailList[i][key])) && (!savedMap.has(detailList[i][key]))){
                    itemMap.set(detailList[i][key], 1);
                    options += "<option>" + detailList[i][key] + "</option>";
                }
            }
        }
    }
    $(id).html("");
    $(id).append(options);
}