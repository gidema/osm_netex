import OsmNetwork from '@networks/network';

describe('Network', () => {
  it('should create an instance', () => {
    expect(new OsmNetwork()).toBeTruthy();
  });
});
