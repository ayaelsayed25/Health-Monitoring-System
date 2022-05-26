// import axios from 'axios';
import React, { useState } from 'react';
import DateTimePicker from 'react-datetime-picker';
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

   
  const [result, setResult] = useState("");

  async function getData () {

    const y_start = start.getFullYear();
    let m_start = start.getMonth() + 1; // Months start at 0!
    let d_start = start.getDate();

    if (d_start < 10) d_start = '0' + d_start;
    if (m_start< 10) m_start = '0' + m_start;

    let startday = d_start + '-' + m_start + '-' + y_start;

    const y_end = end.getFullYear();
    let m_end = end.getMonth() + 1; // Months start at 0!
    let d_end = end.getDate();

    if (d_end < 10) d_end = '0' + d_end;
    if (m_end< 10) m_end = '0' + m_end;

    let endday = d_end + '-' + m_end + '-' + y_end;
    var parameters={
      Start : parseInt(start.getHours()*60+start.getMinutes()),
      End   : parseInt(end.getHours()*60+end.getMinutes()),
      startDay: startday,
      endDay : endday
    }



    axios.get('http://localhost:8085/query/'+parameters.Start+"/"+parameters.End+"/"+parameters.startDay+"/"+parameters.endDay)
    .then(response =>  {setResult(response.data.replace(/(?:\r\n|\r|\n)/g, '<br/>')); 
      // console.log(str)
      // var index = str. indexOf("s")

      // var str1 = str. substring(0, index)
      // str = str.substring(index)
      // index = str. indexOf("s")
      // var str2 = str. substring(0, index)
      // str = str.substring(index)
      // index = str. indexOf("s")
      // var str3 = str. substring(0, index)
      // str = str.substring(index)
      // index = str. indexOf("s")
      // var str4 = str. substring(0, index)
      // str = str.substring(index)
      // setResult(str1 + "\n" + str2 + "\n" + str3 + "\n" + str4)
      // console.log(result)
    }
      )
    .catch(error => {

        console.error('There was an error!', error);
    });
  }

  return (

    

      <div style={{ backgroundImage: `url("/job473-kwan-02-d.jpg")` ,width : 2000,height :1000 ,padding :200 }}> 

        <div>
          <DateTimePicker onChange={setStart} value={start} />
        </div>

        <div>
          <DateTimePicker onChange={setEnd} value={end} />
        </div>


        <button onClick={() => getData()}>Get Data</button> 

        <div>
      {result}
      </div>


      </div>

      
     
   
   
   );
  }
  
  export default App;
  