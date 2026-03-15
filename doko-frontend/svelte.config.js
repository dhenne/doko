import adapter from '@sveltejs/adapter-static';

/** @type {import('@sveltejs/kit').Config} */
const config = {
  kit: {
    adapter: adapter({
      pages: '../doko-backend/src/main/resources/static',
      assets: '../doko-backend/src/main/resources/static',
      fallback: undefined,
      precompress: false,
      strict: true
    })
  }
};

export default config;
 
