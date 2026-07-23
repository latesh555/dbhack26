import axios from "axios";

const getRecentUploads = () => {
  const link = "https://science-pasta-conch.ngrok-free.dev/step-details";
  return axios.get(link,{
  headers: {
    "ngrok-skip-browser-warning": "true",
  },
});
};


const getSummary = (requestId) => {
  const link = `https://science-pasta-conch.ngrok-free.dev/analysis/${requestId}`;
  return axios.get(link,{
  headers: {
    "ngrok-skip-browser-warning": "true",
  },
});
};

const backend = {
  getRecentUploads,
  getSummary,
};

export default backend;