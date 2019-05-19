// copy demo.js 只要用到的id的部分
var enLang = {
    name  : "en",
    month : ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"],
    weeks : [ "SUN","MON","TUR","WED","THU","FRI","SAT" ],
    times : ["Hour","Minute","Second"],
    timetxt: ["Time","Start Time","End Time"],
    backtxt:"Back",
    clear : "Clear",
    today : "Now",
    yes   : "Confirm",
    close : "Close"
}
//常规选择
jeDate("#startTime",{
    language:enLang,
    format: "YYYY-MM-DD hh:mm:ss",
    isinitVal:true
});
jeDate("#endTime",{
    language:enLang,
    format: "YYYY-MM-DD hh:mm:ss",
    isinitVal:true
});