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
 
  // const sportsData = [
  //   { Id: 'query1', query: 'The mean-CPU utilization for each service' },
  //   { Id: 'query2', query: 'The mean Disk utilization for each service' },
  //   { Id: 'query3', query: 'The mean RAM utilization for each service' },
  //   { Id: 'query4', query: 'The peak time of utilization for CPU' },
  //   { Id: 'query5', query: 'The peak time of utilization for RAM' },
  //   { Id: 'query6', query: 'The peak time of utilization for Disk' },
  //   { Id: 'query7', query: 'The count of health messages received for each service' }
  // ];
  
  // const dateValue = new Date(new Date().getFullYear(), new Date().getMonth(), 14);
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
    const response = await fetch('http://192.168.1.10:8080/query');
    const body = await response.json();
    console.log(body);
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
{/* 
        <DateTimePicker
          renderInput={(props) => <TextField {...props} />}
          label="Start"
          value={dateValue}
          onChange={(newValue) => {
            setValue(newValue);
          }}
        />

        <DateTimePicker
          renderInput={(props) => <TextField {...props} />}
          label="End"
          value={dateValue}
          onChange={(newValue) => {
            setValue(newValue);
          }}
        /> */}


        {/* <DatePickerComponent
          id='end' 
          placeholder="Enter end Date"
          value={dateValue}
          format="dd-MM-yyyy"
        /> */}


        <button onClick={() => getData()}>Get Data</button> 

      </div>

      <Table data={dataTable} column={column} />
    </span>
   
  );
}

export default App;