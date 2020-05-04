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
function calculateGradient(colorHex,gradientNumber) {
    var radix = 16;
    var r = parseInt(colorHex.slice(1, 3), radix),
        g = parseInt(colorHex.slice(3, 5), radix),
        b = parseInt(colorHex.slice(5, 7), radix),
        a = parseInt(colorHex.slice(7, 9), radix) / 255 || 1;
    let red = r + (gradientNumber*0.5) * (255 - r);
    let green =g + (gradientNumber*0.5) * (255 - g);
    let blue = b + (gradientNumber*0.5) * (255 - b);

    return RGBAToHexA(red,green,blue,a);
}
function generateSchedule(viewName, renderStart, renderEnd) {
    ScheduleList = [];
    steps.forEach(function (step) {
        let schedule = new ScheduleInfo();
        let calendar = findCalendar(String(step['device']['deviceType']['id']));
        schedule.id = chance.guid();
        schedule.calendarId = calendar.id;
        schedule.title = 'schedule ' + step['id'];
        schedule.body = 'name:    ' + step['device']['devicename'] + "<br>"
            + 'type:    ' + step['device']['deviceType']['deviceTypeName'] + "<br>"
            + 'comment: ' + step['device']['comment'] + "<br>";
        ;
        schedule.isReadOnly = false;
        schedule.start = new Date(step['start'] + 'T' + step['startHour']);
        schedule.end = new Date(step['end'] + 'T' + step['endHour']);
        let gradientNumber=0;
        for(let current = 0;current <ScheduleList.length;current++){
            if(ScheduleList[current].calendarId=== calendar.id){
                gradientNumber++;
            }
        }
        schedule.color = calendar.color;
        schedule.bgColor = calculateGradient(calendar.bgColor,gradientNumber);
        schedule.dragBgColor = calculateGradient(calendar.dragBgColor,gradientNumber);
        schedule.borderColor = calculateGradient(calendar.borderColor,gradientNumber);
        schedule.category = 'time';

        ScheduleList.push(schedule);

    })

}

