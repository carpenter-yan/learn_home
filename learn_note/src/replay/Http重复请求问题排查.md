##问题描述
  潜客挖掘需求中需要通过http请求圈选目标用户，算法反馈每次http请求了2次。
##问题分析
  发送http请求源码如下，通过部分封装的HttpUtils工具类发送，内部为Apache实现。
```
    Map<String, Object> lookalikeInput = new HashMap<>();
    lookalikeInput.put("userpin_seed_file", seedUserFile);
    lookalikeInput.put("userpin_filter_file", diffCate);
    lookalikeInput.put("max_userpin_potential_count", condition.getHopeSize() > 0 ? condition.getHopeSize() : potentialUserMax);
    HttpUtils httpUtils = new HttpUtils(searchUrl, HttpUtils.METHOD_POST);
    httpUtils.addHeader("content-type", "application/json; charset=utf-8");
    httpUtils.setRequestEntity(new StringRequestEntity(JSON.toJSONString(lookalikeInput), "application/json", "utf-8"));
    log.info("目标用户圈选x3:url={}, lookalike={}", JSON.toJsonString(httpUtils), JSON.toJSONString(lookalikeInput));
    String response = httpUtils.execute();
    log.info("目标用户圈选x4:response={}", response);
```
   httpUtils.execute()的源码如下
```
	public HttpMethodBase executeRequest() throws Exception {
		HttpMethodBase method = null;
		if ("POST".equals(getMethod())) {
			method = new PostMethod(getUrl());
		} else if ("PUT".equals(getMethod())) {
			method = new PutMethod(getUrl());
		} else if ("DELETE".equals(getMethod())) {
			method = new DeleteMethod(getUrl());
		} else if ("HEAD".equals(getMethod())) {
			method = new HeadMethod(getUrl());
		} else if ("OPTIONS".equals(getMethod())) {
			method = new OptionsMethod(getUrl());
		} else if ("TRACE".equals(getMethod())) {
			method = new TraceMethod(getUrl());
		} else {
			method = new GetMethod(getUrl());
		}
		// 设置头信息
		method.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.120 Safari/537.36");
		method.setRequestHeader("Connection", "Keep-Alive");
		method.setRequestHeader("Accept-Charset", getCharset());
		// 设置参数信息
		if ("POST".equals(getMethod())) {
			String contentType = header.get("Content-Type");
			if ("multipart/form-data".equals(contentType)) {
				((PostMethod) method).setRequestEntity(new MultipartRequestEntity(getPartList(), method.getParams()));
				header.remove("Content-Type");
			} else {
				((PostMethod) method).addParameters(getPairList());
			}
			if (requestEntity != null) {
				((PostMethod) method).setRequestEntity(requestEntity);
			}
		} else if (StringUtils.isBlank(method.getQueryString())) {
			method.setQueryString(getQueryString());
		}

		for (Entry<String, String> entry : header.entrySet()) {
			method.setRequestHeader(entry.getKey(), entry.getValue());
		}

		org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
		HttpClientParams clientParams = client.getParams();
		clientParams.setContentCharset(getCharset());

		HttpConnectionManagerParams params = client.getHttpConnectionManager().getParams();
		params.setConnectionTimeout(connectTimeout);
		params.setSoTimeout(readTimeout);

		// 发生异常自动重试3次
		int retry = Constants.getInt("httpclient.retryCount", 3);
		if (retry > 0) {
			params.setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, true));
		}
		int rescode = client.executeMethod(method);
		if (rescode != HttpStatus.SC_OK) {
			String err = "";
			try {
				err = method.getResponseBodyAsString();
			} catch (Exception e) {
				err = "无法读取响应消息";
			}
			throw new IllegalAccessException(MessageFormatter.stringFormat("Http request error code :%s ,content :%s", rescode, err));
		}
		return method;
	}
```
    最开始怀疑是自动重试（HttpMethodParams.RETRY_HANDLER）的问题。将retry设置为0后，问题依旧
    通过ideal的断点跟踪工具发现代码执行中调用以下方法
```
	public long getContentLength() {
		try {
			HttpMethodBase method = executeRequest();
			return method.getResponseContentLength();
		} catch (Exception e) {
			return Long.MAX_VALUE;
		}
	}
```
    原因很显然，JSON.toJsonString(httpUtils)时调用了getContentLength方法。去掉日志打印问题解决

##结论
    fastjson在解析对象时会对get，set方法解析并调用。今后开发中非属性get/set方法要避免使用get/set开头。toJsonString时最好时对JavaBean类进行转换，其它类型并要注意get/set方法是否会引起以上问题