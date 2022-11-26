
import './App.css';
import React, {Context} from 'react'
import data from './data.json'

import WorkFlow from './components/WorkFlow'



const config = data.data.nodeConfig
console.log(config)
function App() {
  return (
    <div className="App">
      <WorkFlow config={config} />
    </div>
  );
}

export default App;
