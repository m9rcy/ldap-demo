import React, { createContext, useContext, useEffect, useState } from 'react';

// Create a context for the configuration
const ConfigContext = createContext();

// Custom hook to use the configuration context
export const useConfig = () => useContext(ConfigContext);

// Configuration provider component
export const ConfigProvider = ({ children }) => {
  const [config, setConfig] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchConfig = async () => {
      try {
        const response = await fetch('/api/config'); // Replace with your API endpoint
        const data = await response.json();
        setConfig(data);
      } catch (err) {
        setError(err);
      } finally {
        setLoading(false);
      }
    };

    fetchConfig();
  }, []);

  return (
    <ConfigContext.Provider value={{ config, loading, error }}>
      {children}
    </ConfigContext.Provider>
  );
};


import React from 'react';
import ReactDOM from 'react-dom';
import { ConfigProvider } from './ConfigContext'; // Adjust the path
import App from './App';

ReactDOM.render(
  <ConfigProvider>
    <App />
  </ConfigProvider>,
  document.getElementById('root')
);


import React from 'react';
import { useConfig } from './ConfigContext'; // Adjust the path

const Header = () => {
  const { config, loading, error } = useConfig();

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error.message}</div>;

  return (
    <header>
      <h1>{config?.appName}</h1>
      <p>{config?.welcomeMessage}</p>
    </header>
  );
};

export default Header;

// fetch data at root level
import React, { useEffect, useState } from 'react';
import Header from './Header';

const App = () => {
  const [config, setConfig] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchConfig = async () => {
      try {
        const response = await fetch('/api/config');
        const data = await response.json();
        setConfig(data);
      } catch (err) {
        setError(err);
      } finally {
        setLoading(false);
      }
    };

    fetchConfig();
  }, []);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error.message}</div>;

  return (
    <div>
      <Header config={config} />
      {/* Other components */}
    </div>
  );
};

export default App;

