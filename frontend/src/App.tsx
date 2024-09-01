import React, { useEffect, useState } from 'react';
import logo from './logo.svg';
import './App.css';
import axios from 'axios';

function App() {
  const [message, setMessage] = useState([]);

  const [data, setData] = useState('')
  useEffect(() => {
    axios.get('/demo/api/data')
    .then(res => setData(res.data))
    .catch(err => console.log(err))
}, []);

  useEffect(() => {
    fetch("/api/movie")
      .then((response) => {
        return response.json();
      })
      .then(function (data) {
        setMessage(data);
      })
  }, []);


  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.tsx</code> and save to reload22.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>

      </header>
    </div>
  );
}

export default App;
