'use strict';

/* eslint-disable require-jsdoc, no-unused-vars */
let calendarType = 1; // user



var ScheduleList = [];








function ScheduleInfo() {
    this.id = null;
    this.calendarId = null;

    this.title = null;
    this.body = null;
    this.isAllday = false;
    this.start = null;
    this.end = null;
    this.category = '';
    this.dueDateClass = '';

    this.color = null;
    this.bgColor = null;
    this.dragBgColor = null;
    this.borderColor = null;
    this.customStyle = '';

    this.isFocused = false;
    this.isPending = false;
    this.isVisible = true;
    this.isReadOnly = false;
    this.goingDuration = 0;
    this.comingDuration = 0;
    this.recurrenceRule = '';
    this.state = '';

    this.raw = {
        memo: '',
        hasToOrCc: false,
        hasRecurrenceRule: false,
        location: null,
        class: 'public', // or 'private'
        creator: {
            name: '',
            avatar: '',
            company: '',
            email: '',
            phone: ''
        }
    };
}
function RGBAToHexA(r,g,b,a) {
    r = r.toString(16);
    g = g.toString(16);
    b = b.toString(16);
    a = Math.round(a * 255).toString(16);

    if (r.length == 1)
        r = "0" + r;
    if (g.length == 1)
        g = "0" + g;
    if (b.length == 1)
        b = "0" + b;
    if (a.length == 1)
        a = "0" + a;

    return "#" + r + g + b + a;
}
function calculateGradient(colorHex,gradientNumber,gradientPercentage) {
    var radix = 16;
    var r = parseInt(colorHex.slice(1, 3), radix),
        g = parseInt(colorHex.slice(3, 5), radix),
        b = parseInt(colorHex.slice(5, 7), radix),
        a = parseInt(colorHex.slice(7, 9), radix) / 255 || 1;
    let red = r + (gradientNumber*gradientPercentage) * (255 - r);
    let green =g + (gradientNumber*gradientPercentage) * (255 - g);
    let blue = b + (gradientNumber*gradientPercentage) * (255 - b);

    return RGBAToHexA(Math.floor(red),Math.floor(green),Math.floor(blue),Math.floor(a));
}
function generateSchedule(viewName, renderStart, renderEnd) {
    ScheduleList = [];
    if (showOwnItems) {
        console.log(userSteps)
        showItems(userSteps,false);
    }
    if (showOtherItems) {
        showItems(otherSteps,true);
    }
}
function showItems(array,showusername){
    array.forEach(function (step,index) {
        let schedule = new ScheduleInfo();
        let calendar = findCalendar(String(step['device']['deviceType']['id']));
        schedule.id = chance.guid();
        schedule.calendarId = calendar.id;
        let comment = "";
        if(step['device']['comment']==null){
            comment = translations.comment +':  '+translations.none
        }else{
            comment = translations.comment +':  '+step['device']['comment']
        }
        let username;
        if(showusername){
            username = translations.user+':  ' + usernames[index]+ "<br>";
        }else{
            username = ''
        }
        let title;
        if(step['stepType']==null||step['stepType']['name']==null){
            title = step['device']['devicename'];
        }else{
            title = step['stepType']['name'];
            console.log(step['stepType'])
        }
        schedule.title = title;

        schedule.body = username
        + translations.name+':  ' + step['device']['devicename'] + "<br>"
        +translations.type+':  ' + step['device']['deviceType']['deviceTypeName'] + "<br>"
        +comment+ "<br>";
        ;

        schedule.isReadOnly = false;
        schedule.start = new Date(step['start'] + 'T' + step['startHour']);
        schedule.end = new Date(step['end'] + 'T' + step['endHour']);
        let gradientNumber = -1;
        let numberOfDevicesOfType = 0;
        devices.forEach(function (device) {
            if (device['deviceType']['id'] == calendar.id) {
                numberOfDevicesOfType++;
                if (step['device']['id'] == device['id']) {
                    if (gradientNumber == -1) {
                        gradientNumber = numberOfDevicesOfType;
                    }
                }
            }
        });
        gradientNumber--;
        if (gradientNumber < 0) {
            gradientNumber++;
        }
        schedule.color = calendar.color;
        schedule.bgColor = calculateGradient(calendar.bgColor, gradientNumber, 1 / numberOfDevicesOfType);
        schedule.dragBgColor = calculateGradient(calendar.dragBgColor, gradientNumber, 1 / numberOfDevicesOfType);
        schedule.borderColor = calculateGradient(calendar.borderColor, gradientNumber, 1 / numberOfDevicesOfType);
        schedule.category = 'time';
        ScheduleList.push(schedule);
    })
}


