let client = document.getElementById('mqttclient');

let mqtt = require('mqtt')
let client  = mqtt.connect('mqtt://test.mosquitto.org')

client.on('connect', function () {
  client.subscribe('presence')
  client.publish('presence', 'Hello mqtt')
})

client.on('message', function (topic, message) {
  // message is Buffer
  client.botao.innerHTML = message.toString();
  console.log(message.toString())
  client.end()
})
