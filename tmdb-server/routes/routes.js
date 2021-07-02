'use strict';

const request = require('request');
const fetch = require('node-fetch');
const { json, urlencoded } = require('express');
const API_KEY = 'a21eabff9a50895a28e14f57af081a11'
//const PATH = __dirname + '/hw8-front-end/'

module.exports = function(app, router) { 


    app.get('/api/watch/:media/:id', async (req, res, next) => 
    {
        const result = await fetch(
            `https://api.themoviedb.org/3/${req.params.media}/${req.params.id}/videos?api_key=${API_KEY}&language=en-US&page=1`
        );
        const resultJSON = await result.json()
        res.json(watchProcessor(resultJSON['results']));
    });

    /** CATEGORY
     * :media - tv or movie 
     * :category - now_playing, top_rated, popular
     * 
     */

    app.get('/api/category/:media/:category', async (req, res, next) => {
        const result = await fetch(
            `https://api.themoviedb.org/3/${req.params.media}/${req.params.category}?api_key=${API_KEY}&language=en-US&page=1`
        );
        let resultJSON = await result.json();
        if(req.params.category === 'now_playing') {
            res.json(processor(resultJSON['results'].slice(0,6), req.params.media));
        } else {
            res.json(processor(resultJSON['results'].slice(0,10), req.params.media));
        }
    });

    /** RECOMMENDATIONS  
     * :media - tv or movie
     * :id - id of film or television
     * :likeness - similar or recommendations
     */
    app.get('/api/recommendations/:media/:id/:likeness', async (req, res, next) => {
        const result = await fetch(
            `https://api.themoviedb.org/3/${req.params.media}/${req.params.id}/${req.params.likeness}?api_key=${API_KEY}&language=en-US&page=1`
        );
        const resultJSON = await result.json()
        res.json(processor(resultJSON['results'].slice(0,10), req.params.media));
    });

    /** CREDITS
    * :media - tv or movie
    * :id - id of film or show
    */

     app.get('/api/cast/:media/:id', async (req, res, next) => {
        const result = await fetch(
            `https://api.themoviedb.org/3/${req.params.media}/${req.params.id}/credits?api_key=${API_KEY}&language=en-US&page=1`
        );
        const resultJSON = await result.json()
        res.json(detailsProcessorCast(resultJSON['cast'].slice(0,6)));
    });

     /** REVIEWS
      * :media - movie or tv
      * :id - id of media
     */
     app.get('/api/reviews/:media/:id', async (req, res, next) => {
        const result = await fetch(
            `https://api.themoviedb.org/3/${req.params.media}/${req.params.id}/reviews?api_key=${API_KEY}&language=en-US&page=1`
        );
        const resultJSON = await result.json()
        res.json(reviewsProcessor(resultJSON['results'].slice(0, 3)));
    });

    /** PERSON
    * :id of actor
    */
    app.get('/api/person/:id/', async (req, res, next) => {
        const result = await fetch(
            `https://api.themoviedb.org/3/person/${req.params.id}?api_key=${API_KEY}&language=en-US&page=1`
        );
        const resultJSON = await result.json()
        res.json(detailsProcessorPeople(resultJSON));
    });


    /** TRENDING 
    * :media - tv or movie
    * :id - id of film or show
    */
    app.get('/api/trending/:media/:time', async (req, res, next) => {
        const result = await fetch(
            `https://api.themoviedb.org/3/trending/${req.params.media}/${req.params.time}?api_key=${API_KEY}`
        );
        const resultJSON = await result.json()
        res.json(processor(resultJSON['results'].slice(0,6), req.params.media));
    });

    /** DETAILS  
    * :media - tv or movie
    * :id - id of film or show
    */
    app.get('/api/details/:media/:id', async (req, res, next) => {
        const result = await fetch(
            `https://api.themoviedb.org/3/${req.params.media}/${req.params.id}?api_key=${API_KEY}`
        );
        const resultJSON = await result.json()
        console.log(resultJSON);
        res.json(detailsProcessorMedia(resultJSON, req.params.media));
    });


    /** SEARCH FUNCTIONALITY **/
    /* :search - the term
    **/

    app.get('/api/multi/:search', async (req, res, next) => {
        var object = [];

        const result = await fetch(
            `https://api.themoviedb.org/3/search/multi?api_key=${API_KEY}&language=en-US&query=${req.params.search}`
        );
        var process_results = await result.json();
        process_results = process_results['results'].slice(0,20);    
        
        process_results.forEach(element => {
            if(element['media_type'] !== 'person')
                object.push({
                    id: element['id'],
                    media_type: element['media_type'],
                    name: (element['media_type'] === 'movie') ? element['title'] : element['name'],
                    poster_path: (element['backdrop_path'] !== null) ? 'https://image.tmdb.org/t/p/w500' + element['backdrop_path'] : 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRHnPmUvFLjjmoYWAbLTEmLLIRCPpV_OgxCVA&usqp=CAU',
                    release_date: processReleaseDate(element),
                    rating: element['vote_average'] / 2

                });
            });
                
        res.json(object);
    });
    
    /** CAST ENDPOINTS  */
    app.get('/api/person/:id/external', async (req, res) => {
        const result = await fetch(
            `https://api.themoviedb.org/3/person/${req.params.id}/external_ids?api_key=${API_KEY}&language=en-US&page=1`
        );
        var process_results = await result.json();
      
        return res.json({
            imdb: `http://www.imdb.com/name/${process_results['imdb_id']}`,
            facebook: `http://www.facebook.com/${process_results['facebook_id']}`,
            instagram: `http://www.instagram.com/${process_results['instagram_id']}`,
            twitter: `http://www.twitter.com/${process_results['twitter_id']}`
        });
        
    });

    function processor (process_results, media = 'movie') {
        var object = [];
        process_results.forEach(element => { 
            object.push({
                id: element['id'],
                title: (media === 'movie') ? element['title'] : element['name'],
                poster_path: element['poster_path'] !== null ? 'https://image.tmdb.org/t/p/w500' + element['poster_path'] : 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRHnPmUvFLjjmoYWAbLTEmLLIRCPpV_OgxCVA&usqp=CAU'
            });
        });

        return object;
    }

    function watchProcessor (process_results) {
        var object = [];        
        process_results.forEach(element => { 
            object.push({
                site: element['site'],
                type: element['type'],
                name: element['name'],
                key:  element['key'] !== null ? element['key'] : 'tzkWB85ULJY'
            });
        });

        return object;

    }

    function detailsProcessorMedia(result, media='movie') {
        return {
            title: (media === 'movie') ? result['title'] : result['name'],
            genres: processGenres(result['genres']),
            spoken_languages: processLanguages(result['spoken_languages']),
            release_date: (media === 'movie') ? result['release_date'].substring(0,4) : result['first_air_date'].substring(0,4),
            runtime: (media === 'movie') ? calculateRunTime(result['runtime']) : calculateRunTime(result['episode_run_time']),
            overview: result['overview'],
            vote_average: result['vote_average'],
            tagline: result['tagline'],
            poster_path: result['poster_path'] !== null ? `https://image.tmdb.org/t/p/w500${result['poster_path']}` : 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRHnPmUvFLjjmoYWAbLTEmLLIRCPpV_OgxCVA&usqp=CAU',
            backdrop_path: result['backdrop_path'] !== null ? `https://image.tmdb.org/t/p/w500${result['backdrop_path']}` : 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRHnPmUvFLjjmoYWAbLTEmLLIRCPpV_OgxCVA&usqp=CAU'
        };
        
    }

    function reviewsProcessor(results) {
        var object = [];
        results.forEach(element => {
            object.push({ 
                author: element['author'],
                content: element['content'],
                created_date: new Date(Date.parse(element['created_at'])).toString().slice(0,24),
                url: element['url'],
                rating: element['author_details']['rating'] / 2 || 0,
                avatar_path: processAvatar(element['author_details']['avatar_path'])
            });
       });

       return object;
    }

    function detailsProcessorCast(results) {
        var object = [];
        results.filter(element => element['profile_path'] !== null).forEach(element => {
            if(element['profile_path'] === null) { }
            object.push({
                id: element['id'],
                name: element['name'],
                character: element['character'],
                profile_path: 'https://image.tmdb.org/t/p/w500' + element['profile_path']
            });
        });

        return object;
    }

    function detailsProcessorPeople(results) {
        return {
            birthday: results['birthday'],
            birthplace: results['place_of_birth'],
            gender: processGender(results['gender']),
            name: results['name'],
            homepage: results['homepage'],
            also_known_as: results['also_known_as'],
            homepage: results['homepage'],
            known_for: results['known_for_department'],
            biography: results['biography'],
            profile_path: results['profile_path'] !== null ? 'https://image.tmdb.org/t/p/w500' + results['profile_path'] : 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRHnPmUvFLjjmoYWAbLTEmLLIRCPpV_OgxCVA&usqp=CAU'
        };
        
    }


    function processGenres(genres) {
        var results = [];
        if(genres.length == 0) {
            return "N/A";
        }

        genres.forEach(genre => {
            results.push(' ' + genre['name'])
        });

        return results;
    }

    function processLanguages(languages) { 
        var results = [];
        languages.forEach(language => {
            results.push(language['name'])
        });

        return results;
    }

    function processGender(gender) {
        if(gender === 1) return 'Female';
        else if(gender === 2) return 'Male';
        else return 'Undefined';
    }

    function calculateRunTime(runtime) {
        if(runtime < 60)
        {
            return `${runtime} minutes`;
        }
        var minutes = (runtime % 60);
        var hours = (runtime - minutes) / 60
        return `${hours}hrs ${minutes}mins`
    }

    // This is grody, but it's what we have to do.
    function processAvatar(path) {
        if(path === null) {
            return 'https://secure.gravatar.com/avatar/utEXl2EDiXBK6f41wCLsvprvMg4.jpg';
        }

        if(!path.includes('https://secure.gravatar.com/avatar/')) {
            return 'https://secure.gravatar.com/avatar' + path;
        } else {
            return path.substring(1, path.length);
        }
    }

    function processReleaseDate(element) 
    {
        if(element['media_type'] === 'movie') 
            return (element['release_date']) ? element['release_date'].substring(0,4) : 'N/A';
        else {
            return (element['first_air_date']) ? element['first_air_date'].substring(0,4) : 'N/A'
        }
    }
}