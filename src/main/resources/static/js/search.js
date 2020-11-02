$(function () {
    var content;
    var text = $('#searchtext').val();
    for(var i=0;i<$('.getTitle').length;i++){
        content=$('.getTitle')[i].innerHTML;
        let replaceReg=new RegExp(text,'ig');
            $('.getTitle')[i].innerHTML=content.replace(replaceReg,function(){
                return '<span style="color: #FFC66D">'+arguments[0]+'</span>';
            });
    }
})