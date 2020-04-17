'use strict';

/* eslint-disable require-jsdoc, no-unused-vars */


var CalendarList = [];
var ScheduleList = [];


function CalendarInfo() {
    this.id = null;
    this.name = null;
    this.checked = true;
    this.color = null;
    this.bgColor = null;
    this.borderColor = null;
    this.dragBgColor = null;
}

function addCalendar(calendar) {
    CalendarList.push(calendar);
}

function findCalendar(id) {
    var found;

    CalendarList.forEach(function(calendar) {
        if (calendar.id === id) {
            found = calendar;
        }
    });

    return found || CalendarList[0];
}

function hexToRGBA(hex) {
    var radix = 16;
    var r = parseInt(hex.slice(1, 3), radix),
        g = parseInt(hex.slice(3, 5), radix),
        b = parseInt(hex.slice(5, 7), radix),
        a = parseInt(hex.slice(7, 9), radix) / 255 || 1;
    var rgba = 'rgba(' + r + ', ' + g + ', ' + b + ', ' + a + ')';

    return rgba;
}

(function() {

    deviceTypes.forEach(function (deviceType) {
        let calendar = new CalendarInfo();

        calendar.id = String(deviceType['id']);
        calendar.name = deviceType['deviceTypeName'];
        calendar.color = '#ffffff';
        calendar.bgColor = '#9e5fff';
        calendar.dragBgColor = '#9e5fff';
        calendar.borderColor = '#9e5fff';
        addCalendar(calendar);
    })
})();


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
        schedule.color = calendar.color;
        schedule.bgColor = calendar.bgColor;
        schedule.dragBgColor = calendar.dragBgColor;
        schedule.borderColor = calendar.borderColor;
        schedule.category = 'time';

        ScheduleList.push(schedule);

    })

}

