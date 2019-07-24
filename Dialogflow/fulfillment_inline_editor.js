// See https://github.com/dialogflow/dialogflow-fulfillment-nodejs
// for Dialogflow fulfillment library docs, samples, and to report issues
'use strict';
const axios = require('axios'); 
const {google} = require('googleapis');
const {WebhookClient} = require('dialogflow-fulfillment');
const {Card, Suggestion} = require('dialogflow-fulfillment');
const {dialogflow,SignIn} = require("actions-on-google");
const functions = require('firebase-functions');
const app = dialogflow({
  clientId: '188389004096-jfo00jpmjpdj1cq8hfptpl0bf9fb08sm.apps.googleusercontent.com'
});

const url = 'https://sharedagendaapp.herokuapp.com/createEvent';
let axiosConfig = {
	headers: { 'content-type': 'application/json',
             "Access-Control-Allow-Origin": "*"},
};

let body = {

    "callerId": "sudojimmy@gmail.com",

    "event": {

             "eventname":"",
             "starterId":"sudojimmy@gmail.com",
             "type":"STUDY",
             "startTime":"",
             "endTime":"",
             "repeat": {
             "startDate":"",
             "endDate": "",
             "type":"ONCE"
              },
             "location":"",
             "state":"ACTIVE",
             "description":"",
             "permission": { "type":"PUBLIC" }
             }
};

process.env.DEBUG = 'dialogflow:debug'; // enables lib debugging statements
const default_intent = 'Default Welcome Intent';
const createEvent_intent = 'CreateEvent';
const fallback_intent = 'Default Fallback Intent';

app.intent(default_intent, (conv) => {
	conv.ask("welcome to Time Space");
});

app.intent(fallback_intent, (conv) => {
	conv.ask("I didn't understand your request");
});

app.intent(createEvent_intent, (conv) => {
	  conv.ask("createEvent now");
  	const payload = conv.user.profile.payload;
	console.log(payload);
  if (payload) {
  body.callerId = payload.email;
    body.event.starterId = payload.email;
  } else {
  	conv.ask("please sign in first");
  }
  	const location = conv.parameters.location;
    const name = conv.parameters.name;
    var date = conv.parameters.date.split('T')[0];
    var endTime = new Date(conv.parameters.time);
    endTime.setHours(endTime.getHours()-3);
    var end = endTime.toISOString().split('T')[1].substring(0,5);
    var starttime = conv.parameters.time.split('T')[1].substring(0,5);
    body.event.repeat.startDate = date;
    body.event.repeat.endDate = date;
    body.event.startTime = starttime;
    body.event.endTime = end;
    body.event.eventname = name;
    body.event.location = location;
    console.log("create event");
    console.log(body);
    axios.post(url, body, axiosConfig).then((result) => {

                conv.ask("created event "+name);
    }).catch(function (error) {
    // handle error
    console.log(error);
      conv.ask("created event unsuccess please try again ");
  });
    conv.ask("intent called: created event");
});

exports.dialogflowFirebaseFulfillment = functions.https.onRequest(app);

