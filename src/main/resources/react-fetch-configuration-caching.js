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
      // Check if config is already cached in localStorage
      const cachedConfig = localStorage.getItem('appConfig');
      if (cachedConfig) {
        setConfig(JSON.parse(cachedConfig));
        setLoading(false);
        return;
      }

      try {
        const response = await fetch('/api/config'); // Replace with your API endpoint
        const data = await response.json();
        setConfig(data);
        // Cache the config in localStorage
        localStorage.setItem('appConfig', JSON.stringify(data));
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

//in memory
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

//local storage
useEffect(() => {
  const fetchConfig = async () => {
    try {
      const response = await fetch('/api/config');
      const data = await response.json();

      // Check if the cached version matches the new version
      const cachedConfig = localStorage.getItem('appConfig');
      if (cachedConfig) {
        const { version: cachedVersion } = JSON.parse(cachedConfig);
        if (cachedVersion === data.version) {
          setConfig(JSON.parse(cachedConfig));
          setLoading(false);
          return;
        }
      }

      // Update the cache with the new configuration
      setConfig(data);
      localStorage.setItem('appConfig', JSON.stringify(data));
    } catch (err) {
      setError(err);
    } finally {
      setLoading(false);
    }
  };

  fetchConfig();
}, []);

//periodic cache
useEffect(() => {
  const fetchConfig = async () => {
    try {
      const response = await fetch('/api/config');
      const data = await response.json();
      setConfig(data);
      localStorage.setItem('appConfig', JSON.stringify(data));
    } catch (err) {
      setError(err);
    } finally {
      setLoading(false);
    }
  };

  // Fetch immediately
  fetchConfig();

  // Refetch every 5 minutes
  const interval = setInterval(fetchConfig, 5 * 60 * 1000);

  // Cleanup interval on unmount
  return () => clearInterval(interval);
}, []);


//invalidation
const refreshConfig = () => {
  localStorage.removeItem('appConfig');
  window.location.reload(); // Reload the page to refetch the configuration
};

<button onClick={refreshConfig}>Refresh Configuration</button>