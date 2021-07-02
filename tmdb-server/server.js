'use strict';
const express = require('express');
const path = require('path');
const bodyParser = require('body-parser');
const cors = require('cors');
const corsOptions = {
    origin: 'http://localhost:4200',
    optionsSuccess: 200
}


const app = express();
const routes = require('./routes/routes.js');


const router = express.Router();
const port = process.env.PORT || 8080;
app.use(cors(corsOptions));
app.use(bodyParser.json());         
app.use(bodyParser.urlencoded({ extended: true })); 

routes(app, router);

app.listen(port, () => { console.log(`I'm hosted on ${port}.`); });

