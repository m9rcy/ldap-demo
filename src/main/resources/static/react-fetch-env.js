//constant

// configKeys.ts
export const CONFIG_KEYS = {
  DISPLAY_NAME: 'spring.ui.app.displayName',
  BANNER_COLOR: 'spring.ui.app.bannerColor',
  FOOTER_TEXT: 'spring.ui.app.footerText',
  // Add other keys as needed
} as const;

// envConfig.ts
import { CONFIG_KEYS } from './configKeys';

interface EnvConfig {
  [CONFIG_KEYS.DISPLAY_NAME]?: string;
  [CONFIG_KEYS.BANNER_COLOR]?: string;
  [CONFIG_KEYS.FOOTER_TEXT]?: string;
  // Add other properties as needed
}

export default EnvConfig;

// useEnv.ts
import { useEffect, useState } from 'react';
import { CONFIG_KEYS } from './configKeys';
import EnvConfig from './envConfig';

const useEnv = () => {
  const [config, setConfig] = useState<EnvConfig>({});
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<Error | null>(null);

  useEffect(() => {
    fetch('/api/config/ui')
      .then((response) => {
        if (!response.ok) {
          throw new Error('Failed to fetch configuration');
        }
        return response.json();
      })
      .then((data: EnvConfig) => {
        setConfig(data);
        setLoading(false);
      })
      .catch((error) => {
        setError(error);
        setLoading(false);
      });
  }, []);

  return { config, loading, error };
};

export default useEnv;

import React from 'react';
import useEnv from './useEnv';
import { CONFIG_KEYS } from './configKeys';

const Banner = () => {
  const { config, loading, error } = useEnv();

  if (loading) {
    return <div>Loading banner configuration...</div>;
  }

  if (error) {
    return <div>Error loading banner: {error.message}</div>;
  }

  const displayName = config[CONFIG_KEYS.DISPLAY_NAME] || 'Default App Name';
  const bannerColor = config[CONFIG_KEYS.BANNER_COLOR] || '#ffffff';

  return (
    <div style={{ backgroundColor: bannerColor, padding: '20px', textAlign: 'center' }}>
      <h1>{displayName}</h1>
    </div>
  );
};

export default Banner;

import React from 'react';
import useEnv from './useEnv';
import { CONFIG_KEYS } from './configKeys';

const Footer = () => {
  const { config, loading, error } = useEnv();

  if (loading) {
    return <div>Loading footer configuration...</div>;
  }

  if (error) {
    return <div>Error loading footer: {error.message}</div>;
  }

  const footerText = config[CONFIG_KEYS.FOOTER_TEXT] || 'Default Footer Text';

  return (
    <footer style={{ padding: '10px', textAlign: 'center', backgroundColor: '#f0f0f0' }}>
      <p>{footerText}</p>
    </footer>
  );
};

export default Footer;

const useEnv = () => {
  const [config, setConfig] = useState<EnvConfig>({
    [CONFIG_KEYS.DISPLAY_NAME]: 'Default App Name',
    [CONFIG_KEYS.BANNER_COLOR]: '#ffffff',
    [CONFIG_KEYS.FOOTER_TEXT]: 'Default Footer Text',
  });
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<Error | null>(null);

  useEffect(() => {
    fetch('/api/config/ui')
      .then((response) => {
        if (!response.ok) {
          throw new Error('Failed to fetch configuration');
        }
        return response.json();
      })
      .then((data: EnvConfig) => {
        setConfig((prevConfig) => ({ ...prevConfig, ...data })); // Merge with defaults
        setLoading(false);
      })
      .catch((error) => {
        setError(error);
        setLoading(false);
      });
  }, []);

  return { config, loading, error };
};

import React from 'react';
import Banner from './Banner';
import Footer from './Footer';

const App = () => {
  return (
    <div>
      <Banner />
      <main>
        <h2>Welcome to the App</h2>
        <p>This is the main content of the application.</p>
      </main>
      <Footer />
    </div>
  );
};

export default App;

//setting default values

const useEnv = () => {
  const [config, setConfig] = useState<EnvConfig>({
    'spring.ui.app.displayName': 'Default App Name',
    'spring.ui.app.bannerColor': '#ffffff',
    'spring.ui.app.footerText': 'Default Footer Text',
  });
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<Error | null>(null);

  useEffect(() => {
    fetch('/api/config/ui')
      .then((response) => {
        if (!response.ok) {
          throw new Error('Failed to fetch configuration');
        }
        return response.json();
      })
      .then((data: EnvConfig) => {
        setConfig((prevConfig) => ({ ...prevConfig, ...data })); // Merge with defaults
        setLoading(false);
      })
      .catch((error) => {
        setError(error);
        setLoading(false);
      });
  }, []);

  return { config, loading, error };
};