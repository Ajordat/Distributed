
function updateNodeValue(node_id, value) {
    document.getElementById(node_id).innerHTML = node_id + ': ' + value;
}

$(document).ready(function(){
    var nodes = Object.freeze(['A1', 'A2', 'A3', 'B1', 'B2', 'C1', 'C2']);
    var ws = [];
    var initialPort = 3050;

    for (let index in nodes) {
        ws[index] = new WebSocket("ws://localhost:" + (initialPort + +index));
        //var exampleSocket = new WebSocket("ws://localhost:".concat(3000 + nodes[index]));
        updateNodeValue(nodes[index], initialPort + +index);
    }
});
