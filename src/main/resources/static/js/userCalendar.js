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

