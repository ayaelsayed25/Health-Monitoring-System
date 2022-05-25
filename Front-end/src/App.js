// import axios from 'axios';
import React, { useState } from 'react';
import DateTimePicker from 'react-datetime-picker';
// import { DatePickerComponent } from "@syncfusion/ej2-react-calendars";
// import {ComboBoxComponent} from '@syncfusion/ej2-react-dropdowns';
import Table from './components/table';
import './App.css';
import axios from 'axios';

function App() {
  const divStyle = {
    margin: 100,
    width: 250
  };

  const [dataTable, setDataTable] = useState([]);
  const column = [
    { heading: 'Service Name', value: 'name' },
    { heading: 'Result', value: 'result' },
  ];
  const [start, setStart] = useState(new Date());
  const [end, setEnd] = useState(new Date());

   
  async function getData (){
    console.log("HHHHHHHHHHHHHHHHHHHHHHHHHHHHh")
    var parameters = {
      startDate :start.toString(),
      endDate : end.toString()
    }
    // console.log(parameters)

    // axios.post('http://localhost:8080/query', parameters)

    var dateTime = JSON.stringify(parameters);
    // const data = {foo:1, bar:2};

    // axios.get(`http://localhost:8080/query`)
    // .then(response => console.log(response.data))
    // .catch(error => {
    //   this.setState({ errorMessage: error.message });
    //   console.error('There was an error!', error);
    // });
    await axios.get(`http://localhost:8082/query`)
    .then((res) => console.log(res))
  }  

  return (

    <span>

      <div style={divStyle} >

        
        {/* <ComboBoxComponent 
          id='choice' 
          placeholder='select a query'
          dataSource={sportsData}
          fields={{value:"Id", text:"query"}}
        />

        <DatePickerComponent  
          id='start' 
          placeholder="Enter start Date"
          value={dateValue}
          format="dd-MM-yyyy"
        /> */}
        <div>
          <DateTimePicker onChange={setStart} value={start} />
        </div>

        <div>
          <DateTimePicker onChange={setEnd} value={end} />
        </div>


        <button onClick={() => getData()}>Get Data</button> 

      </div>

      <Table data={dataTable} column={column} />
    </span>
   
  );
}

export default App;