# XRebelHub Wicket Endpoints

Better names for [XRebelHub](https://zeroturnaround.com/software/xrebel-hub/) endpoints for [Apache Wicket](https://wicket.apache.org/) pages.

By default XRebel Hub uses the request URIs to name endpoints. For Wicket however this rarely produces useful results. Only is case of bookmarkable URLs the resulting URI will be usable. The generated URIs loose all information about which wicket pages or actions are executed. 

## Implementation

Via Wicket's [RequestLogger](https://svn.apache.org/repos/asf/wicket/common/site/apidocs/1.4/org/apache/wicket/protocol/http/RequestLogger.html) interface you have proper access to the page and action (request target) which handled the HTTP request. 

With the recent addition to the [XRebel Hub agent](https://manuals.zeroturnaround.com/xrebelhub/use/advanced.html#custom-profiling) you are able to set names based on request or response headers. 

By using a customized ``RequestLogger`` instance we inject a special header which contains a better named endpoint for XRebel Hub to use.

This implementation targets Wicket 1.4, but can easily be applied to newer Wicket versions.

## Usage

To use this new ``RequestLogger`` class you simply need to return it from your custom wicket ``Application`` instance.

```java
public class MyWebApplication extends WebApplication {

	// ...
	
	@Override
	protected IRequestLogger newRequestLogger() {
		if (Boolean.getBoolean("xrebelhub")) {
			return new com.mpobjects.labs.wicket.xrebelhub.XrhRequestLogger();
		}
		else {
			return super.newRequestLogger();
		}
	}
} 
```

This would only use the specialized request logger when the system property ``xrebelhub`` is set to ``true``.

You also need to create a [customer profiling](https://manuals.zeroturnaround.com/xrebelhub/use/advanced.html#custom-profiling) configuration for the agent to use the special HTTP header as endpoint name.  

```xml
<profilingConfig>
	<entryPoint pattern="* /appname/*"  setName="${responseHeader:x-rebelhub-endpoint}"
</profilingConfig>
```

## Settings


## License

    Copyright 2017 MP Objects BV
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

