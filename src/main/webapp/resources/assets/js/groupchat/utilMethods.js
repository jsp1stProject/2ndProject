dayjs.locale('ko');
dayjs.extend(dayjs_plugin_advancedFormat);

export const utilMethods = {
  formatMessageTime(datetime) {
    const now = dayjs();
    const msgTime = dayjs(datetime);
    return msgTime.isSame(now, 'day')
      ? msgTime.format('A h:mm')
      : msgTime.format('YYYY-MM-DD A h:mm');
  }
};
