## Asteroid Radar

The app fetches all the asteroids from the NASA server API's and shows us all the nearing earth asteroids that are dangerous and those which are not.

## Updated Features
1. Updated all the libraries to the latest running versions.
2. Added new hebrew language (RTL).
3. Provided all dynamic image descriptions.
4. Refactored styles in the fragments.
5. Refactored all the colors/dimens/strings.
6. Implemented offline caching in Database as the single source of truth.
7. Added workmanager to download new this week's Astroids and ImageOfTheDay.
8. Implemented single source of truth by fetching all data to show in screen only from the local database.
9. Replaced the API_KEY with the DEMO_KEY. Please change the API_KEY in the WebService class. Or you can use the default DEMO_KEY.

## Walkthrough
<img src="assets/walkthrough.gif" width="428" height="926" />

## Report Issues

Notice any issues with a repository? Please file a github issue in the repository.

License
=======

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
