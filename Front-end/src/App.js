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
    // console.log("HHHHHHHHHHHHHHHHHHHHHHHHHHHHh")
    // var parameters = {
    //   startDate :start.toString(),
    //   endDate : end.toString()
    // }
    var parameters={
      Start : start.toString(),
      End   : end.toString()
    }


    console.log(parameters)

    axios.post('http://167.172.39.122:8082/query', parameters)
    .then(response =>  setDataTable(response.data))
    .catch(error => {
        this.setState({ errorMessage: error.message });
        console.error('There was an error!', error);
    });
  }

  return (

    <span>

      <div style={divStyle} >

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
  