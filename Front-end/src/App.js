import axios from 'axios';
import { useState } from 'react';
import { DatePickerComponent } from "@syncfusion/ej2-react-calendars";
import {ComboBoxComponent} from '@syncfusion/ej2-react-dropdowns';
import Table from './components/table';
import './App.css';

function App() {
  const divStyle = {
    margin: 100,
    width: 250
  };
 
  const sportsData = [
    { Id: 'query1', query: 'The mean-CPU utilization for each service' },
    { Id: 'query2', query: 'The mean Disk utilization for each service' },
    { Id: 'query3', query: 'The mean RAM utilization for each service' },
    { Id: 'query4', query: 'The peak time of utilization for CPU' },
    { Id: 'query5', query: 'The peak time of utilization for RAM' },
    { Id: 'query6', query: 'The peak time of utilization for Disk' },
    { Id: 'query7', query: 'The count of health messages received for each service' }
  ];
  
  const dateValue = new Date(new Date().getFullYear(), new Date().getMonth(), 14);
  const [dataTable, setDataTable] = useState([]);
  const column = [
    { heading: 'Service Name', value: 'name' },
    { heading: 'Result', value: 'result' },
  ];
   
  function getData (){
    var start = document.getElementById('start').value;
    var end = document.getElementById('end').value;
    var query = document.getElementById('choice').value;

    var parameters = {
      Query : query,
      Start :start,
      End : end
    }
    console.log(parameters)

    axios.post('http://localhost:8085/times', parameters)
    .then(response => setDataTable(response.data))
    .catch(error => {
      this.setState({ errorMessage: error.message });
      console.error('There was an error!', error);
    });
  }  

  return (

    <span>

      <div style={divStyle} >

        
        <ComboBoxComponent 
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
        />


        <DatePickerComponent
          id='end' 
          placeholder="Enter end Date"
          value={dateValue}
          format="dd-MM-yyyy"
        />


        <button onClick={getData}>Get Data</button> 

      </div>

      <Table data={dataTable} column={column} />
    </span>
   
  );
}

export default App;