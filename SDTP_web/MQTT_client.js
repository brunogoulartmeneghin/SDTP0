let campo = document.getElementById('mqttclient');

let mqtt = require('mqtt')
let client  = mqtt.connect('http://127.0.0.1:3000/')

client.on('connect', function () {
  client.subscribe('meutopico')
  client.publish('meutopico', 'Hello mqtt')
})

client.on('message', function (topic, message) {
  // message is Buffer
  campo.innerHTML = message.toString();
  client.end()
})
