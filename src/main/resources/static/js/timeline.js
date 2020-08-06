    function setTime() {
        var days, hours, minutes, seconds;
        var build_time = new Date('08 03,2020 16:00:00').getTime();
        var nowTime = new Date().getTime();
        var s_time = (nowTime - build_time) / 1000;
        days = Math.floor(s_time / 86400);
        hours = Math.floor((s_time - days * 86400) / 3600);
        minutes = Math.floor((s_time - days * 86400 - hours * 3600) / 60);
        seconds = Math.floor(s_time % 60);
        $('#days').html(days);
        $('#hours').html(hours);
        $('#minutes').html(minutes);
        $('#seconds').html(seconds);

        //document.getElementById('Build_time').innerHTML = '本站已在此等候您 '+days+' 天 '+hours+' 时 '+minutes+' 分 '+seconds+' 秒 ';
    }
    setInterval('setTime()',1000);