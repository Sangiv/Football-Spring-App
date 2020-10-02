fetch('http://localhost:8901/player/readAll/')
  .then(
    function(response) {
      if (response.status !== 200) {
        console.log('Looks like there was a problem. Status Code: ' +
          response.status);
        return;
      }

      // Examine the text in the response
      response.json().then(function(playerData) {
        console.log(playerData);

        let table = document.querySelector("table");
        let dataHead = Object.keys(playerData[0]);

        createTableHead(table, dataHead);
        createTableBody(table, playerData);
      });
    }
  )
  .catch(function(err) {
    console.log('Fetch Error :-S', err);
  });

  function createTableHead(table, dataHead) {
      let tableHead = table.createTHead();
      let row = tableHead.insertRow();

      for(let keys of dataHead){
          let th = document.createElement("th");
          let text = document.createTextNode(keys);
          th.appendChild(text);
          row.appendChild(th);
      }

      let thView = document.createElement("th");
      let textView = document.createTextNode("View");
      thView.appendChild(textView);
      row.appendChild(thView);

      let thDel = document.createElement("th");
      let textDel = document.createTextNode("Delete");
      thDel.appendChild(textDel);
      row.appendChild(thDel);
      
  }


  function createTableBody(table, playerData) {
      for(let playerRecord of playerData) {
          let row = table.insertRow();
          for(values in playerRecord) {
              console.log(playerRecord[values]);
              let cell = row.insertCell()
              let text = document.createTextNode(playerRecord[values]);
              cell.appendChild(text);

          }
          
          let cellView = row.insertCell();
          let viewButton = document.createElement("a");
          let viewText = document.createTextNode("VIEW");
          viewButton.className = "btn btn-info";
          viewButton.href = 'record.html?id='+playerRecord.id
          viewButton.appendChild(viewText);
          cellView.appendChild(viewButton);

          let cellDell = row.insertCell();
          let DelButton = document.createElement("a");
          let DelText = document.createTextNode("Delete");
          DelButton.className = "btn btn-danger";
        //   DelButton.href = `record.html?`
          DelButton.appendChild(DelText);
          cellDell.appendChild(DelButton);
      }
  }